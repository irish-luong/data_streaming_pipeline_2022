package models;

//Data model of invoice
case class Invoice(
                    invoiceNo: String,
                    stockCode: String,
                    description: String,
                    quantity: Int,
                    unitPrice: Int,
                    customerID: Int,
                    country: String,
                    InvoiceDate: String
                  )


