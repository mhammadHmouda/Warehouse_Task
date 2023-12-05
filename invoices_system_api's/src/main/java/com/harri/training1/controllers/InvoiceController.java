package com.harri.training1.controllers;

import com.harri.training1.models.dto.InvoiceDto;
import com.harri.training1.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/general")
    public ResponseEntity<?> getInvoices() {
        List<InvoiceDto> dtos = invoiceService.getInvoices();

        return ResponseEntity.ok(dtos);
    }
}