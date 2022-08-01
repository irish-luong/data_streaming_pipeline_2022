# Pure Python libraries
import datetime
import logging

# 3PL modules
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

    await service.public_invoice(invoice_json)

    logging.info(invoice)
    return invoice

