package com.CabService.CabService.controller;

import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Add new booking
    @PostMapping("/add-booking")
    public String addBooking(@RequestBody Booking booking) {
        return customerService.addBooking(booking);
    }

    // View bookings
    @GetMapping("/view-bookings/{customerId}")
    public List<Booking> viewBookings(@PathVariable int customerId) {
        return customerService.getBookingsByCustomer(customerId);
    }

    // Finalize booking (send to admin)
    @PutMapping("/finalize-booking/{bookingId}")
    public String finalizeBooking(@PathVariable int bookingId) {
        return customerService.finalizeBooking(bookingId);
    }

    // View bill
    @GetMapping("/view-bill/{bookingId}")
    public Bill viewBill(@PathVariable int bookingId) {
        return customerService.getBill(bookingId);
    }
}
