package com.harri.training1.services;

import com.harri.training1.exceptions.InvoiceNotAddedException;
import com.harri.training1.exceptions.InvoiceNotExistException;
import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.models.dto.InvoiceDto;
import com.harri.training1.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);
    private final InvoiceRepository invoiceRepository;
    private final AutoMapper<Invoice, InvoiceDto> mapper;


    public void addInvoice(List<InvoiceDto> invoices) {
        try {
            List<Invoice> invoiceList = new ArrayList<>();
            invoices.forEach(invoiceDto -> {
                Invoice invoice = mapper.toModel(invoiceDto, Invoice.class);
                invoiceList.add(invoice);
            });
            invoiceRepository.save(invoiceList);
        } catch (Exception e) {
            LOGGER.error("Something went wrong when add new invoice: " + e.getMessage());
            throw new InvoiceNotAddedException(e.getMessage());
        }
    }

    public List<InvoiceDto> findById(Long id) {
        List<Invoice> invoices = invoiceRepository.findById(id);

        if (invoices.isEmpty()) {
            LOGGER.error("No any invoice with id = " + id);
            throw new InvoiceNotExistException("No invoice exists for this id: " + id);
        }

        List<InvoiceDto> invoiceList = new ArrayList<>();
        invoices.forEach(invoice -> {
            InvoiceDto invoiceDto = mapper.toDto(invoice, InvoiceDto.class);
            invoiceList.add(invoiceDto);
        });

        return invoiceList;
    }

    public List<InvoiceDto> findAll() {
        List<Invoice> invoices = invoiceRepository.findAll();

        if (invoices.isEmpty()) {
            LOGGER.error("No any invoice in the system!");
            throw new InvoiceNotExistException("No invoice exists in the system!");
        }

        List<InvoiceDto> invoicesDto = invoices.stream()
                .map(invoice -> mapper.toDto(invoice, InvoiceDto.class))
                .toList();

        if (invoicesDto.isEmpty()){
            LOGGER.error("All invoices are deleted from the system!");
            throw new InvoiceNotExistException("All invoices are deleted from the system!");
        }

        return invoicesDto;
    }

    public List<InvoiceDto> findByUserId(Long id) {
        List<Invoice> invoices = invoiceRepository.findByUserId(id);

        if (invoices == null || invoices.isEmpty()) {
            throw new InvoiceNotExistException("No invoices exist for the user with id: " + id);
        }

        List<InvoiceDto> invoicesDto = invoices.stream()
                .map(invoice -> mapper.toDto(invoice, InvoiceDto.class))
                .toList();

        if(invoicesDto.isEmpty()){
            LOGGER.error("No any invoice in the system for the user with id = " + id);
            throw new InvoiceNotExistException("No any invoice in the system for the user with id = " + id);
        }

        return invoicesDto;
    }

    public void deleteById(Long invoiceId) {
        Long result = invoiceRepository.deleteById(invoiceId);

        if (result == 0) {
            LOGGER.error("No any invoice with id = " + invoiceId);
            throw new InvoiceNotExistException("No invoice exists for this id: " + invoiceId);
        }
    }
}