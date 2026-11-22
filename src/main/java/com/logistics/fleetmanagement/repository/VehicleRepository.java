
package com.logistics.fleetmanagement.repository;

import com.logistics.fleetmanagement.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
