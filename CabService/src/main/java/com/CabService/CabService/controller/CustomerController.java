package com.CabService.CabService.controller;

import com.CabService.CabService.dto.BookingRequest;
import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @DeleteMapping("/delete-booking/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable int bookingId) {
        try {
            customerService.deleteBooking(bookingId);
            return ResponseEntity.ok("Booking deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Add new booking
    @PostMapping("/add-booking")
    public ResponseEntity<?> addBooking(@RequestBody BookingRequest bookingRequest) {
        try {
            String result = customerService.addBooking(bookingRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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
