
package com.logistics.fleetmanagement.service;

import com.logistics.fleetmanagement.entity.Booking;
import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    void cancelBooking(Long id);
}
