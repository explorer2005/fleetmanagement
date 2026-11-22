package com.logistics.fleetmanagement.repository;

import com.logistics.fleetmanagement.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByShipmentId(Long shipmentId);
}