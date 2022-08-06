# Pure Python libs
import logging
import socket
from typing import Dict, Any
from confluent_kafka import Producer as BaseProducer


class Producer:

    def __init__(
            self,
            bootstrap_servers: str
    ):
        self.properties = {
            "bootstrap.servers": bootstrap_servers,
            "client.id": socket.gethostname()
        }

        self.producer = BaseProducer(**self.properties)

    def __ack(self, err, msg):
        if err:
            logging.error("Failed to deliver message: {} - {}".format(err.__str__(), msg.__str__()))
        else:
            logging.debug("Message produced: {}".format(msg.__str__()))

    async def async_produce(self, topic: str, key: Any, value: Any):
        self.producer.produce(topic, key=key, value=value, callback=self.__ack)

    def produce(self, topic: str, key: Any, value: Any):
        self.producer.produce(topic, key=key, value=value)
