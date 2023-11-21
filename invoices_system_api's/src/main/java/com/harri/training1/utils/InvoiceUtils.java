package com.harri.training1.utils;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.harri.training1.models.entities.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class InvoiceUtils {

    public Invoice mapToInvoice(TableResult result) {
        FieldValueList row = result.iterateAll().iterator().next();
        return buildInvoice(row);
    }

    public List<Invoice> mapToInvoices(TableResult result) {
        List<Invoice> invoices = new ArrayList<>();
        result.iterateAll().forEach(row -> {
            Invoice invoice = buildInvoice(row);
            invoices.add(invoice);
        });
        return invoices;
    }
    private Invoice buildInvoice(FieldValueList row) {
        Invoice invoice = new Invoice();
        invoice.setId(row.get("Id").getLongValue());
        invoice.setAmount(((float) row.get("Amount").getDoubleValue()));
        invoice.setCreatedAt(row.get("CreatedAt").getStringValue());
        invoice.setUserId(row.get("UserId").getLongValue());
        FieldValue invoiceItemField = row.get("InvoiceItem");
        invoice.setItem(Item.fromStructField(invoiceItemField));

        return invoice;
    }

}
