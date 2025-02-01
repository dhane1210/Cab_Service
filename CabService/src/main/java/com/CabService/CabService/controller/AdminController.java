package com.CabService.CabService.controller;

import com.CabService.CabService.model.Car;
import com.CabService.CabService.model.Driver;
import com.CabService.CabService.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Login endpoint
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return adminService.authenticate(username, password);
    }

    // Update discounts and taxes
    @PutMapping("/update-pricing")
    public String updatePricing(@RequestParam double discount, @RequestParam double tax) {
        return adminService.updatePricing(discount, tax);
    }

    // Manage cars
    @PostMapping("/add-car")
    public String addCar(@RequestBody Car car) {
        return adminService.addCar(car);
    }

    @GetMapping("/available-cars")
    public List<Car> getAvailableCars() {
        return adminService.getAvailableCars();
    }

    // Manage drivers
    @PostMapping("/add-driver")
    public String addDriver(@RequestBody Driver driver) {
        return adminService.addDriver(driver);
    }

    @GetMapping("/available-drivers")
    public List<Driver> getAvailableDrivers() {
        return adminService.getAvailableDrivers();
    }

    // Connect car to driver
    @PostMapping("/assign-car-to-driver")
    public String assignCarToDriver(@RequestParam int driverId, @RequestParam int carId) {
        return adminService.assignCarToDriver(driverId, carId);
    }
}