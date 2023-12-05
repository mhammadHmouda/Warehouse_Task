package com.harri.training1.utils;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.harri.training1.models.entities.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class InvoiceUtils {

    public Invoice mapToInvoice(TableResult result) {
        Invoice invoice = new Invoice();
        List<Item> items = new ArrayList<>();

        result.iterateAll().forEach(row -> {
            if (invoice.getId() == null) {
                invoice.setId(row.get("Id").getLongValue());
                invoice.setAmount((float) row.get("Amount").getDoubleValue());
                invoice.setCreatedAt(row.get("CreatedAt").getStringValue());
                invoice.setUserId(row.get("UserId").getLongValue());
            }

            items.add(buildItem(row));
        });

        invoice.setItems(items);
        return invoice;
    }

    public List<Invoice> mapToInvoices(TableResult result) {
        Map<Long, Invoice> invoiceMap = new HashMap<>();

        result.iterateAll().forEach(row -> {
            Long id = row.get("Id").getLongValue();
            Invoice invoice = invoiceMap.computeIfAbsent(id, k -> {
                Invoice newInvoice = new Invoice();
                newInvoice.setId(id);
                newInvoice.setAmount((float) row.get("Amount").getDoubleValue());
                newInvoice.setCreatedAt(row.get("CreatedAt").getStringValue());
                newInvoice.setUserId(row.get("UserId").getLongValue());
                newInvoice.setItems(new ArrayList<>());
                return newInvoice;
            });

            invoice.getItems().add(buildItem(row));
        });

        return new ArrayList<>(invoiceMap.values());
    }

    private Item buildItem(FieldValueList row) {
        Item item = new Item();
        item.setName(row.get("ItemName").getStringValue());
        item.setPrice((float) row.get("ItemPrice").getDoubleValue());
        item.setQuantity((int) row.get("ItemQuantity").getLongValue());

        return item;
    }
}
