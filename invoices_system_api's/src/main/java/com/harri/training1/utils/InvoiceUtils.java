package com.harri.training1.utils;

import com.harri.training1.models.dto.InvoiceDto;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.models.entities.Item;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class InvoiceUtils {

    public List<InvoiceDto> mapToDtos(List<Invoice> invoices){
        Map<Long, InvoiceDto> invoiceMap = new HashMap<>();

        invoices.forEach(invoice -> {
            Long id = invoice.getId();
            InvoiceDto dto = invoiceMap.computeIfAbsent(id, k -> buildInvoiceDto(invoice));
            dto.getItems().add(buildItem(invoice));
        });

        return new ArrayList<>(invoiceMap.values());
    }

    private InvoiceDto buildInvoiceDto(Invoice invoice) {
        InvoiceDto newInvoice = new InvoiceDto();
        newInvoice.setId(invoice.getId());
        newInvoice.setAmount(invoice.getAmount());
        newInvoice.setCreatedAt(invoice.getCreatedAt());
        newInvoice.setUserId(invoice.getUserId());
        newInvoice.setItems(new ArrayList<>());

        return newInvoice;
    }

    private Item buildItem(Invoice invoice) {
        Item item = new Item();
        item.setName(invoice.getItemName());
        item.setPrice(invoice.getItemPrice());
        item.setQuantity(invoice.getItemQuantity());

        return item;
    }
}
