package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Invoice;
import com.logistics.fleetmanagement.entity.Shipment;
import com.logistics.fleetmanagement.repository.InvoiceRepository;
import com.logistics.fleetmanagement.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ShipmentRepository shipmentRepository;

    private static final double TAX_RATE = 0.18;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            ShipmentRepository shipmentRepository) {

        this.invoiceRepository = invoiceRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    @Transactional
    public Invoice generateInvoice(Long shipmentId, Double amount) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        if (shipment.getStatus().equalsIgnoreCase("CANCELLED")) {
            throw new RuntimeException("Cannot generate invoice for cancelled shipment");
        }

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invoice amount must be greater than zero");
        }

        if (invoiceRepository.findByShipmentId(shipmentId).isPresent()) {
            throw new RuntimeException("Invoice already exists for this shipment");
        }

        double tax = amount * TAX_RATE;
        double totalAmount = amount + tax;

        Invoice invoice = new Invoice();

        invoice.setShipment(shipment);
        invoice.setAmount(amount);
        invoice.setTax(tax);
        invoice.setTotalAmount(totalAmount);
        invoice.setStatus("GENERATED");

        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    @Override
    public Invoice getInvoiceByShipmentId(Long shipmentId) {
        return invoiceRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
}