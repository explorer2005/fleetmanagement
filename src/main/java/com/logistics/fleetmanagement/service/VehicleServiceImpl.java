
package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Vehicle;
import com.logistics.fleetmanagement.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Vehicle existingVehicle = getVehicleById(id);

        existingVehicle.setRegistrationNumber(updatedVehicle.getRegistrationNumber());
        existingVehicle.setVehicleType(updatedVehicle.getVehicleType());
        existingVehicle.setCapacity(updatedVehicle.getCapacity());
        existingVehicle.setStatus(updatedVehicle.getStatus());

        return vehicleRepository.save(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }
}
