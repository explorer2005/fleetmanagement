
package com.logistics.fleetmanagement.repository;

import com.logistics.fleetmanagement.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
