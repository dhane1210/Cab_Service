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

    @PostMapping("/verify-customer")
    public String verifyCustomer(@RequestParam int driverId, @RequestParam int customerId) {
        return driverService.verifyCustomer(driverId, customerId);
    }

    @GetMapping("/view-assigned-trip/{driverId}")
    public Booking viewAssignedTrip(@PathVariable int driverId) {
        return driverService.getAssignedTrip(driverId);
    }
}
