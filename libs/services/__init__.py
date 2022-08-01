import logging


class InvoiceService:

    def __init__(self):
        pass

    async def public_invoice(self, data: dict) -> None:
        """
        Function create invoice item to DB
        """

        logging.info(data)
