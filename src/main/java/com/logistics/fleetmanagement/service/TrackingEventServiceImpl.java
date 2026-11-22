package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Shipment;
import com.logistics.fleetmanagement.entity.TrackingEvent;
import com.logistics.fleetmanagement.repository.ShipmentRepository;
import com.logistics.fleetmanagement.repository.TrackingEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrackingEventServiceImpl implements TrackingEventService {

    private final TrackingEventRepository trackingEventRepository;
    private final ShipmentRepository shipmentRepository;

    public TrackingEventServiceImpl(
            TrackingEventRepository trackingEventRepository,
            ShipmentRepository shipmentRepository) {

        this.trackingEventRepository = trackingEventRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void deleteTrackingEvent(Long id) {

        if (!trackingEventRepository.existsById(id)) {
            throw new RuntimeException("Tracking event not found");
        }

        trackingEventRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TrackingEvent addTrackingEvent(TrackingEvent trackingEvent) {

        if (trackingEvent.getShipment() == null ||
                trackingEvent.getShipment().getId() == null) {
            throw new RuntimeException("Shipment ID is required");
        }

        Shipment shipment = shipmentRepository.findById(
                        trackingEvent.getShipment().getId())
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        String currentStatus = shipment.getStatus();
        String newStatus = trackingEvent.getStatus();

        boolean validTransition =
                (currentStatus.equalsIgnoreCase("CREATED")
                        && newStatus.equalsIgnoreCase("PICKED_UP"))
                        ||
                        (currentStatus.equalsIgnoreCase("PICKED_UP")
                                && newStatus.equalsIgnoreCase("IN_TRANSIT"))
                        ||
                        (currentStatus.equalsIgnoreCase("IN_TRANSIT")
                                && newStatus.equalsIgnoreCase("DELIVERED"));

        if (!validTransition) {
            throw new RuntimeException(
                    "Invalid shipment status transition: "
                            + currentStatus + " -> " + newStatus);
        }
        trackingEvent.setShipment(shipment);

        shipment.setStatus(trackingEvent.getStatus());

        shipmentRepository.save(shipment);

        return trackingEventRepository.save(trackingEvent);
    }

    @Override
    public List<TrackingEvent> getTrackingEventsByShipment(Long shipmentId) {

        if (!shipmentRepository.existsById(shipmentId)) {
            throw new RuntimeException("Shipment not found");
        }

        return trackingEventRepository
                .findByShipmentIdOrderByEventTimeAsc(shipmentId);
    }
}