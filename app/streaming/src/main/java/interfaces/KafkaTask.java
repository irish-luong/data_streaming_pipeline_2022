package interfaces;

public interface KafkaTask {

    public long getCurrentOffset();

    public boolean isFinished();

    public void stop();

    public long waitForCompletion();
}
