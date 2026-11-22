
package com.logistics.fleetmanagement.repository;

import com.logistics.fleetmanagement.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByShipmentIdAndStatus(Long shipmentId, String status);
}
