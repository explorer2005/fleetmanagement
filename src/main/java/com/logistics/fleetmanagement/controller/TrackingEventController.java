package com.logistics.fleetmanagement.controller;

import com.logistics.fleetmanagement.entity.TrackingEvent;
import com.logistics.fleetmanagement.service.TrackingEventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
public class TrackingEventController {

    private final TrackingEventService trackingEventService;

    public TrackingEventController(TrackingEventService trackingEventService) {
        this.trackingEventService = trackingEventService;
    }

    @PostMapping
    public TrackingEvent addTrackingEvent(@RequestBody TrackingEvent trackingEvent) {
        return trackingEventService.addTrackingEvent(trackingEvent);
    }

    @GetMapping("/shipment/{shipmentId}")
    public List<TrackingEvent> getTrackingEvents(
            @PathVariable Long shipmentId) {

        return trackingEventService.getTrackingEventsByShipment(shipmentId);
    }

    @DeleteMapping("/{id}")
    public String deleteTrackingEvent(@PathVariable Long id) {
        trackingEventService.deleteTrackingEvent(id);
        return "Tracking event deleted successfully";
    }
}