# Pure Python libs
import time
import logging
import asyncio
from concurrent import futures

# Third-party modules
from confluent_kafka import Consumer as BaseConsumer


from pydantic import BaseModel


class Consumer:

    consumer = None

    def __init__(
            self,
            kafka_client_conf: Dict[str, any]
            topic_name_pattern: str,
            message_handler: callable
            is_avro: bool = False,
            sleep_secs: float = 5,
            consume_timeout_secs: float = 0.1,
    ):
    """
        Args:

            kafka_client_conf (dict): 
                - Docs: docs https://docs.confluent.io/platform/current/clients/confluent-kafka-python/html/index.html#kafka-client-configuration
                - Examples: {
                        "group.id": "streaming",
                        "enable.auto.commit": False,
                        "bootstrap.servers": "broker_1:9092,broker_2:9092",
                        "max.poll.interval.ms": 6000,
                        "auto.offset.reset": "latest"
                    }

    """

        self.consumer = None
        self.sleep_secs = sleep_secs
        self.message_handler = message_handler
        self.broker_properties = kafka_client_conf
        self.topic_name_pattern = topic_name_pattern
        self.consume_timeout_secs = consume_timeout_secs


    def subscribe(self):
         # Start consumer
        self.consumer = BaseConsumer(self.broker_properties)

        self.consumer.subscribe(
            [self.topic_name_pattern], 
            on_assign=self.on_assign, on_revoke=self.on_revoke
        )
        logging.info(f"Subscribe topic {self.topic_name_pattern}")

        return self


    def __del__(self):
        if self.consumer is not None:
            self.close()

    def __repr__(self):
        return f"Consumer of topic {self.topic_name_pattern}"

    def close(self):
        self.consumer.close()
        logging.info(f"Close consume message from topic {self.topic_name_pattern}")

    def on_assign(self, consumer, partitions):
        """Callback when topic assignment takes place"""

        for partition in partitions:
            logging.info(f"[Topic {self.topic_name_pattern}]- \
                            Partition {partition.partition} is assigned for worker")

        consumer.assign(partitions)

    def on_revoke(self, consumer, partitions):
        """Callback when topic revoke takes place"""

        for partition in partitions:
            logging.info(f"[Topic {self.topic_name_pattern}]- \
                            Partition {partition.partition} is revoked for worker")

    async def async_consume(self):
        """Asynchronously consume data from a kafka topic"""

        while True:
            num_results = 1
            # In case get a message then consume else sleep then switch to new co-routine
            count_success = 0
            while num_results > 0 and count_success < 1000:
                num_results = self._consume()
                if num_results:
                    count_success += 1

            await asyncio.sleep(self.sleep_secs)

    def sync_consume(self):
        """Synchronously consume data from a kafka topic"""
        while True:
            num_results = 1
            while num_results > 0:
                num_results = self._consume()
            time.sleep(self.sleep_secs)

    def concurrency_consume(self, max_worker: int, callback: callable):
        """
        Method process message in a Thread Pool
        :param max_worker: maximum workers in pool
        :type max_worker: integer
        :param callback: callable to handle after message processed
        :type callback: callable
        """
        # with futures.ThreadPoolExecutor(max_worker) as pool:
        while True:
            message = self.consumer.poll(self.consume_timeout_secs)
            if message is None:
                pass
            elif message.error():
                logging.error(f"Got error {message.error()} when "
                              f"consuming message from topic {self.topic_name_pattern}")
            else:
                self.message_handler(message)
                logging.info(f"Submit message at offset {message.offset()} to handler")
                self.consumer.commit(message)

    def _consume(self) -> int:
        """Poll for a message, Return 1 if message is receive, 0 otherwise"""
        message = self.consumer.poll(self.consume_timeout_secs)

        is_success = 0
        if message is None:
            pass
        elif message.error():
            logging.error(f"Got error {message.error()} when consuming message from topic {self.topic_name_pattern}")
        else:
            try:
                self.message_handler(message)
                is_success = 1
                logging.info(f"Processed Topic {message.topic()} | offset {message.offset()} | "
                             f"partition {message.partition()}")
            except Exception as e:
                logging.error(f"Got error when handle offset {message.offset()} "
                              f"of topic {self.topic_name_pattern}: "
                              f"Type {type(e).__name__} occurred. Arguments:{e.args}")
            finally:
                self.consumer.commit(message)

        return is_success
