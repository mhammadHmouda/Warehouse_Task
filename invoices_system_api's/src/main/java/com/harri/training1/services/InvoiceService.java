package com.harri.training1.services;

import com.harri.training1.exceptions.InvoiceNotExistException;
import com.harri.training1.models.dto.InvoiceDto;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.repositories.InvoicesRepository;
import com.harri.training1.utils.InvoiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoicesRepository invoicesRepository;
    private final InvoiceUtils invoiceUtils;

    public List<InvoiceDto> getInvoices(){
        try {
            List<Invoice> invoices = invoicesRepository.executeQuery();
            return invoiceUtils.mapToDtos(invoices);
        }catch (Exception e){
            throw new InvoiceNotExistException("Invoices not found: " + e.getMessage());
        }
    }
}