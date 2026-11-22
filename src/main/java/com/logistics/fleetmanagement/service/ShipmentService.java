
package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Shipment;
import java.util.List;

public interface ShipmentService {

    Shipment createShipment(Shipment shipment);

    List<Shipment> getAllShipments();

    Shipment getShipmentById(Long id);

    List<Shipment> getShipmentsByCustomerId(Long customerId);

    Shipment updateShipment(Long id, Shipment shipment);

    void deleteShipment(Long id);
}
