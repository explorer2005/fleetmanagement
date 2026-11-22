
package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Driver;
import java.util.List;

public interface DriverService {

    Driver createDriver(Driver driver);

    List<Driver> getAllDrivers();

    Driver getDriverById(Long id);

    Driver updateDriver(Long id, Driver driver);

    void deleteDriver(Long id);
}
