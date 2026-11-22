
package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.*;
import com.logistics.fleetmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShipmentRepository shipmentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            ShipmentRepository shipmentRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository) {

        this.bookingRepository = bookingRepository;
        this.shipmentRepository = shipmentRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {

        if (booking.getShipment() == null ||
                booking.getShipment().getId() == null ||
                booking.getDriver() == null ||
                booking.getDriver().getId() == null ||
                booking.getVehicle() == null ||
                booking.getVehicle().getId() == null) {
            throw new RuntimeException("Shipment, driver and vehicle IDs are required");
        }

        Shipment shipment = shipmentRepository.findById(
                        booking.getShipment().getId())
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        Driver driver = driverRepository.findById(
                        booking.getDriver().getId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Vehicle vehicle = vehicleRepository.findById(
                        booking.getVehicle().getId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (shipment.getStatus().equalsIgnoreCase("DELIVERED") ||
                shipment.getStatus().equalsIgnoreCase("CANCELLED")) {
            throw new RuntimeException("Shipment cannot be booked");
        }

        if (bookingRepository.existsByShipmentIdAndStatus(
                shipment.getId(), "CONFIRMED")) {
            throw new RuntimeException("Shipment already has an active booking");
        }

        if (!driver.getStatus().equalsIgnoreCase("AVAILABLE")) {
            throw new RuntimeException("Driver is not available");
        }

        if (!vehicle.getStatus().equalsIgnoreCase("AVAILABLE")) {
            throw new RuntimeException("Vehicle is not available");
        }

        if (vehicle.getCapacity() < shipment.getWeight()) {
            throw new RuntimeException("Vehicle capacity is insufficient");
        }

        booking.setShipment(shipment);
        booking.setDriver(driver);
        booking.setVehicle(vehicle);
        booking.setStatus("CONFIRMED");

        driver.setStatus("ASSIGNED");
        vehicle.setStatus("ASSIGNED");
        shipment.setStatus("BOOKED");

        driverRepository.save(driver);
        vehicleRepository.save(vehicle);
        shipmentRepository.save(shipment);

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {

        Booking booking = getBookingById(id);

        if (!booking.getStatus().equalsIgnoreCase("CONFIRMED")) {
            throw new RuntimeException("Booking is not active");
        }

        booking.setStatus("CANCELLED");

        booking.getDriver().setStatus("AVAILABLE");
        booking.getVehicle().setStatus("AVAILABLE");
        booking.getShipment().setStatus("CREATED");

        bookingRepository.save(booking);
    }
}
