# Pure Python Libraries
import datetime

# Framework modules
import logging

from pydantic import BaseModel, Field, validator


class InvoiceItem(BaseModel):
    InvoiceNo: int
    StockCode: str
    Description: str
    Quantity: int
    UnitPrice: str
    CustomerID: str
    Country: str


class InvoiceItemInput(InvoiceItem):

    InvoiceDate: str = Field(description="Format %d/%m/%Y %H:%M:%S")

    @validator('InvoiceDate')
    def validate_invoice_date(cls, v):
        try:
            date = datetime.datetime.strptime(v, "%d/%m/%Y %H:%M:%S")
        except:
            raise ValueError('Invoice date must be in format %d/%m/%Y %H:%M:%S')
        else:
            return date.strftime("%Y-%m-%d %H:%M:%S")


class InvoiceItemOutput(InvoiceItem):

    InvoiceDate: str = Field(description="Format %Y-%m-%d %H:%M:%S")
