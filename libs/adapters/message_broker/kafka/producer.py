# Pure Python libs
import logging
import socket
from typing import Dict, Any
from confluent_kafka import Producer as BaseProducer, Message


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

    @staticmethod
    def __ack(err, msg: Message):
        if err:
            logging.error("Failed to deliver message: {} - {}".format(err.__str__(), msg.__str__()))
        else:
            logging.debug("Message produced: key: {} to partition {}".format(msg.key(), msg.partition()))

    async def async_produce(self, topic: str, key: Any, value: Any):
        self.producer.produce(topic, key=key, value=value, callback=self.__ack)

        self.producer.poll(1)

    def produce(self, topic: str, key: Any, value: Any):
        self.producer.produce(topic, key=key, value=value)
