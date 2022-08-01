# Framework modules
import datetime

from fastapi.testclient import TestClient

# Project modules
from app.api import create_app

app = create_app()
client = TestClient(app)


def test_create_invoice_success():

    payload = client.post('/api/v1/invoice',
                          headers={'Authorization': 'Bearer abc'},
                          json={
                              "InvoiceNo": 0,
                              "StockCode": "string",
                              "Description": "string",
                              "Quantity": 0,
                              "InvoiceDate": "5/12/1987 21:00",
                              "UnitPrice": "string",
                              "CustomerID": "string",
                              "Country": "string"
                          })

    assert payload.status_code == 200
    res = payload.json()
    assert datetime.datetime.strptime(res['InvoiceDate'], '%Y-%m-%d %H:%M')