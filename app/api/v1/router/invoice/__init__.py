# Pure Python libraries
import datetime
import json
import logging

# 3PL modules
import time

from dependency_injector.wiring import inject, Provide

# Framework modules
from fastapi import APIRouter, Depends
from fastapi.encoders import jsonable_encoder

# Project modules
from app.api.containers import Container, InvoiceService
from app.api.v1.router.invoice.model import InvoiceItemInput, InvoiceItemOutput

# Init router objects
router = APIRouter(
    prefix='/api/v1/invoice',
    tags=['invoice'],
    responses={404: {"message": "Invoice not found"}}
)


@router.post('', response_model=InvoiceItemOutput)
@inject
async def create_invoice(
        invoice: InvoiceItemInput,
        service: InvoiceService = Depends(Provide[Container.invoice_service])
):

    invoice_json = jsonable_encoder(invoice)
    logging.info(invoice_json)
    await service.public_invoice('test', time.time().__str__(), json.dumps(invoice_json))

    return invoice

