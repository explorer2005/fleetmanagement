package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Invoice;
import java.util.List;

public interface InvoiceService {

    Invoice generateInvoice(Long shipmentId, Double amount);

    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(Long id);

    Invoice getInvoiceByShipmentId(Long shipmentId);
}