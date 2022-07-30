# Pure Python libs
import json
from typing import Dict, Any
from kafka import KafkaProducer


class Producer:

    def __init__(
            self,
            bootstrap_servers: str
            properties: Dict[str, Any]
    ):

        """
            Args:
                properties (dict): 
                    Kafka producer configurations
                    Example: 
                        "bootstrap_servers": bootstrap_servers,
                        "key_serializer": str.encode,
                        "value_serializer": lambda v: json.dumps(v).encode('utf-8')
                    }

        """

        self.properties = properties

        self.producer = KafkaProducer(**self.properties)

    def produce(self, topic: str, key: Any, value: Any):
        self.producer.send(topic, key=key, value=value)
