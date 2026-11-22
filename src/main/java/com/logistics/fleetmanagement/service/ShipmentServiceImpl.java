
package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Customer;
import com.logistics.fleetmanagement.entity.Shipment;
import com.logistics.fleetmanagement.repository.CustomerRepository;
import com.logistics.fleetmanagement.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final CustomerRepository customerRepository;

    public ShipmentServiceImpl(
            ShipmentRepository shipmentRepository,
            CustomerRepository customerRepository) {
        this.shipmentRepository = shipmentRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Shipment createShipment(Shipment shipment) {

        Long customerId = shipment.getCustomer().getId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        shipment.setCustomer(customer);

        return shipmentRepository.save(shipment);
    }

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
    }

    @Override
    public List<Shipment> getShipmentsByCustomerId(Long customerId) {
        return shipmentRepository.findByCustomerId(customerId);
    }

    @Override
    public Shipment updateShipment(Long id, Shipment updatedShipment) {
        Shipment existingShipment = getShipmentById(id);

        existingShipment.setDescription(updatedShipment.getDescription());
        existingShipment.setWeight(updatedShipment.getWeight());
        existingShipment.setPickupLocation(updatedShipment.getPickupLocation());
        existingShipment.setDeliveryLocation(updatedShipment.getDeliveryLocation());
        existingShipment.setStatus(updatedShipment.getStatus());

        return shipmentRepository.save(existingShipment);
    }

    @Override
    public void deleteShipment(Long id) {
        Shipment shipment = getShipmentById(id);
        shipmentRepository.delete(shipment);
    }
}
