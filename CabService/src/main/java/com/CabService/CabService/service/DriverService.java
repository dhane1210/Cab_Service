package com.CabService.CabService.service;

import com.CabService.CabService.model.Booking;
import com.CabService.CabService.repo.BookingRepository;
import com.CabService.CabService.repo.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public String verifyCustomer(int driverId, int customerId) {
        Booking booking = bookingRepository.findActiveBookingByCustomer(customerId);

        if (booking != null && booking.getAssignedDriver() != null && booking.getAssignedDriver().getDriverId() == driverId) {
            booking.setStatus("Verified");
            bookingRepository.save(booking);
            return "Customer verified successfully";
        }

        return "Verification failed";
    }

    public Booking getAssignedTrip(int driverId) {
        return bookingRepository.findAssignedBookingByDriver(driverId);
    }
}
