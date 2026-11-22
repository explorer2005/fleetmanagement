package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.TrackingEvent;
import java.util.List;

public interface TrackingEventService {

    TrackingEvent addTrackingEvent(TrackingEvent trackingEvent);

    List<TrackingEvent> getTrackingEventsByShipment(Long shipmentId);

    void deleteTrackingEvent(Long id);
}