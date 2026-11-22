
package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Driver;
import com.logistics.fleetmanagement.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    @Override
    public Driver updateDriver(Long id, Driver updatedDriver) {
        Driver existingDriver = getDriverById(id);

        existingDriver.setName(updatedDriver.getName());
        existingDriver.setPhone(updatedDriver.getPhone());
        existingDriver.setLicenseNumber(updatedDriver.getLicenseNumber());
        existingDriver.setStatus(updatedDriver.getStatus());

        return driverRepository.save(existingDriver);
    }

    @Override
    public void deleteDriver(Long id) {
        Driver driver = getDriverById(id);
        driverRepository.delete(driver);
    }
}
