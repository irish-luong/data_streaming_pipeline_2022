package entity;

// Pure java modules
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

// 3PL modules
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;


// Project modules
import interfaces.KafkaTask;

public class ConsumingTask implements Runnable, KafkaTask {

    // Store all records if this task
    private final List<ConsumerRecord<String, String>> records;

    // Implement state if this task use volatile to sync between multiple thread
    private volatile boolean started = false;
    private volatile boolean stopped = false;
    private volatile boolean finished = false;

    // Implement completion instance
    private final CompletableFuture<Long> completion = new CompletableFuture<>();
    private final ReentrantLock startStopLock = new ReentrantLock();
    private final AtomicLong currentOffset = new AtomicLong();

    // Implement logger
    private final Logger logger = LoggerFactory.getLogger(ConsumingTask.class);


    public ConsumingTask(List<ConsumerRecord<String, String>> records) {
        this.records = records;
    }

    @Override
    public void run() {
        // Synchronize status with parent thread
        startStopLock.lock();
        if (stopped) {
            return;
        }
        started = true;
        startStopLock.unlock();

        // Process record by record
        for (ConsumerRecord<String, String> record: records) {

            if (stopped) {
                break;
            }
            logger.info(record.offset() + record.value());
        }

        // Mark as complete process
        finished = true;
        completion.complete(currentOffset.get());

    }

    @Override
    public long getCurrentOffset() {
        return currentOffset.get();
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void stop() {
        startStopLock.lock();
        stopped = true;
        if (!started){
            finished = true;
            completion.complete(currentOffset.get());
        }
        startStopLock.unlock();
    }

    @Override
    public long waitForCompletion() {
        try {
            return completion.get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
    }
}
