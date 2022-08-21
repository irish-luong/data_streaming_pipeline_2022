package services;

import interfaces.KafkaTask;
import org.jetbrains.annotations.NotNull;
import org.apache.log4j.Logger;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.clients.consumer.ConsumerRecord;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import entity.ConsumingTask;

/*
* 1. Initialize a consumer object in a new thread
* 2. Set pool size of this consumer to thread pool executor
* 3.
* */
public class ReplicationService implements Runnable, ConsumerRebalanceListener {

    // Kafka topic pattern
    public String topicPattern = null;
    // Kafka consumer service
    private final KafkaConsumer<String, String> consumer;
    // Thread pool executor
    private final ExecutorService executor;

    private final AtomicBoolean stopped = new AtomicBoolean(false);

    private final Map<TopicPartition, ConsumingTask> activeTasks = new HashMap<>();
    private final Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();

    // Implement logger object to implement log
    private final Logger logger = Logger.getLogger(ReplicationService.class);

    // Initialize a last commit time to take a short break. Which avoid
    // calling Kafka too much lead to harass system
    private long lastCommitTime = System.currentTimeMillis();
    private int sleepTimeMS;

    public ReplicationService(String topicPattern, Properties consumerProperties, int poolSize) {
        this.topicPattern = topicPattern;
        this.consumer = new KafkaConsumer<String, String>(consumerProperties);
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public ReplicationService setSleepTimeMS(int durationMS) {
        this.sleepTimeMS = durationMS;
        return this;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(Collections.singleton(this.topicPattern), this);

            logger.info(consumer.toString());
            while(!stopped.get()) {
                // Start an infinity loop to pool message from message broker
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));

                // Handle records fetched
                handleFetchedRecords(records);

                // Check finished tasks to resume consumer for continuing fetch records
                checkActiveTasks();

                // Commit all offset of all assigned partitions
                commitOffset();
            }

        } catch (WakeupException we) {
            if (!stopped.get()) {
                throw we;
            }
        } finally {
            consumer.close();
        }
    }


    /*
    Check whether exist record fetched
    - Create an array to store partitions need to pause for processing message
    - Create consuming tasks
    * */
    public void handleFetchedRecords(@NotNull ConsumerRecords<String, String> records) {
        if (records.count() > 0){
            List<TopicPartition> partitionToPause = new ArrayList<>();
            records
                    .partitions()
                    .forEach(partition -> {

                        // Get list messages of this partition
                        List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                        // Create a new task to push to DB
                        ConsumingTask task = new ConsumingTask(partitionRecords);

                        // Submit task to executor | Mark this partition is handling | Push active tasks
                        executor.submit(task);
                        partitionToPause.add(partition);
                        activeTasks.put(partition, task);
                    });
            // Pause consume from this partition
            consumer.pause(partitionToPause);
        }
    }

    /*
    * Method implement check task complete to resume this partition
    * */
    public void checkActiveTasks() {

        // Initialize a list of partitions complete process
        // to resume continue consuming
        List<TopicPartition> finishedTaskPartitions = new ArrayList<>();

        activeTasks.forEach((partition, task) -> {

            if (task.isFinished()){
                finishedTaskPartitions.add(partition);
            };

            // Get offset if this consumer
            long currentOffset = task.getCurrentOffset();

            // Store to this partition what offset we consume from
            if (currentOffset > 0) {
                offsetsToCommit.put(partition, new OffsetAndMetadata(currentOffset));
            }
        });

        // Remove finished partitions form active tasks and resume partitions
        finishedTaskPartitions.forEach(activeTasks::remove);
        consumer.resume(finishedTaskPartitions);
    }


    /*
    * Method check duration from now to the last time commit offsets
    * If the duration is enough then call commit all offset of partition
    * And clear maps
    * */
    public void commitOffset() {
        try {
            long currentTimeMS = System.currentTimeMillis();
            if ((currentTimeMS - lastCommitTime) > sleepTimeMS) {
                if (!offsetsToCommit.isEmpty()) {
                    consumer.commitSync(offsetsToCommit);
                    offsetsToCommit.clear();
                }

                // Reset the last commit time
                lastCommitTime = currentTimeMS;
            }
        } catch (Exception e) {
            logger.error("Got exception when commit offset!", e);
        }
    }
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

        // 1.
        Map<TopicPartition, KafkaTask> stoppedTasks = new HashMap<>();
        for (TopicPartition partition: partitions) {
            ConsumingTask task = activeTasks.remove(partition);
            if (task != null) {
                task.stop();
                stoppedTasks.put(partition, task);
            }
        }

        // 2.
        Map<TopicPartition, OffsetAndMetadata> revokedPartitions = new HashMap<>();
        stoppedTasks.forEach((partition, task) -> {
            long offset = task.waitForCompletion();
            if (offset > 0) {
                OffsetAndMetadata offsetMetadata = new OffsetAndMetadata(offset);
                revokedPartitions.put(partition, offsetMetadata);
            }
        });

        // 3.
        try {
            consumer.commitSync(revokedPartitions);
        } catch(Exception e) {
            logger.warn("Failed to commit all offsets of revoked partitions");
        }

        for (TopicPartition partition: partitions) {
            logger.info("Partition " + partition.toString() + " revoked");
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition partition: partitions) {
            logger.info("Partition " + partition.toString() + " assigned");
        }
        consumer.resume(partitions);
    }
}
