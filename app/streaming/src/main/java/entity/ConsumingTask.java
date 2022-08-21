package entity;

import java.util.*;

import interfaces.KafkaTask;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class ConsumingTask implements Runnable, KafkaTask {

    public ConsumingTask(List<ConsumerRecord<String, String>> records) {
    }

    @Override
    public void run() {

    }

    @Override
    public long getCurrentOffset() {
        return 0;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {

    }

    @Override
    public long waitForCompletion() {
        return 0;
    }


}
