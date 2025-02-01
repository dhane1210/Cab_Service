package com.CabService.CabService.controller;

import com.CabService.CabService.model.Booking;
import com.CabService.CabService.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

//     Verify customer
    @PostMapping("/verify-customer")
    public String verifyCustomer(@RequestParam int driverId, @RequestParam int customerId) {
        return driverService.verifyCustomer(driverId, customerId);
    }

    // Add trip details
    @PutMapping("/end-trip")
    public String endTrip(@RequestParam int bookingId, @RequestParam double waitingTime) {
        return driverService.completeTrip(bookingId, waitingTime);
    }

    // View assigned trip
    @GetMapping("/view-assigned-trip/{driverId}")
    public Booking viewAssignedTrip(@PathVariable int driverId) {
        return driverService.getAssignedTrip(driverId);
    }
}
