
// Pure Java modules
import java.util.Properties;

// 3PL modules
import org.apache.kafka.clients.consumer.ConsumerConfig;

// Project modules
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import services.ReplicationService;


public class Main {

    private static final Logger logger = Logger.getLogger(ReplicationService.class);

    public static void main(String[] args) {

        BasicConfigurator.configure();

        // Kafka configuration
        Properties kafkaConfig = new Properties();
        kafkaConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        kafkaConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        kafkaConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "streaming");

        logger.info(kafkaConfig);
        ReplicationService svc = new ReplicationService("factor", kafkaConfig, 4);
        Thread t1 = new Thread(svc);
        t1.start();
    }
}
