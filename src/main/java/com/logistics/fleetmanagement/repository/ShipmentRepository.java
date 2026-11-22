
package com.logistics.fleetmanagement.repository;

import com.logistics.fleetmanagement.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findByCustomerId(Long customerId);
}
