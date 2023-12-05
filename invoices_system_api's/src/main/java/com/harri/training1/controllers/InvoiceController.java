package com.harri.training1.controllers;

import com.harri.training1.models.dto.InvoiceDto;
import com.harri.training1.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The InvoiceController class is a REST controller that handles invoice-related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
    private final InvoiceService invoiceService;

    /**
     * Handles the request to add an invoice.
     *
     * @return ResponseEntity with a success message if the invoice is added successfully
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPERUSER', 'USER')")
    public ResponseEntity<?> addInvoice(@RequestBody InvoiceDto invoiceDto) {
        invoiceService.addInvoice(invoiceDto);
        LOGGER.info("Add new invoice.");
        return ResponseEntity.ok("Invoice added successfully!");
    }

    /**
     * Handles the request to retrieve a list of all invoices.
     *
     * @return ResponseEntity containing a list of all invoices
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPERUSER', 'AUDITOR')")
    public ResponseEntity<?> findAll() {
        List<InvoiceDto> invoices = invoiceService.findAll();
        LOGGER.info("Get all invoices.");
        return ResponseEntity.ok(invoices);
    }

    /**
     * Handles the request to retrieve an invoice by ID.
     *
     * @param id   the ID of the invoice to retrieve
     * @return ResponseEntity containing the retrieved invoice
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    /**
     * Handles the request to retrieve a list of invoices by user ID.
     *
     * @param id   the ID of the user
     * @return ResponseEntity containing a list of invoices associated with the user
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable Long id) {
        List<InvoiceDto> invoices = invoiceService.findByUserId(id);
        LOGGER.info("Get invoice by user id = " + id);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Handles the request to delete an invoice by ID.
     *
     * @param id   the ID of the invoice to delete
     * @return ResponseEntity with a success message if the invoice is deleted successfully
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        invoiceService.deleteById(id);
        LOGGER.info("Delete invoice by id = " + id);
        return ResponseEntity.ok("Invoice deleted successfully!");
    }
}