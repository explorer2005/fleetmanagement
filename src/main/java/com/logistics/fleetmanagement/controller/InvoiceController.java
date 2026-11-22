package com.logistics.fleetmanagement.controller;

import com.logistics.fleetmanagement.entity.Invoice;
import com.logistics.fleetmanagement.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/shipment/{shipmentId}")
    public Invoice generateInvoice(
            @PathVariable Long shipmentId,
            @RequestParam Double amount) {

        return invoiceService.generateInvoice(shipmentId, amount);
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @GetMapping("/shipment/{shipmentId}")
    public Invoice getInvoiceByShipmentId(
            @PathVariable Long shipmentId) {

        return invoiceService.getInvoiceByShipmentId(shipmentId);
    }
}