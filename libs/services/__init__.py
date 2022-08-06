import logging


class InvoiceService:

    def __init__(self, producer):
        self.producer = producer

    async def public_invoice(self, topic: str, key: str, data: str) -> None:
        """
        Function create invoice item to DB
        """
        await self.producer.async_produce(topic=topic, key=key, value=data)
