package com.CabService.CabService.controller;


import com.CabService.CabService.dto.BillUpdateRequest;
import com.CabService.CabService.dto.DriverWithCarDetails;
import com.CabService.CabService.model.*;
import com.CabService.CabService.service.AdminService;
import com.CabService.CabService.service.BillService;
import com.CabService.CabService.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BillService billService;
    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/all-bookings")
    public List<Booking> getAllBookings() {
        return adminService.getAllBookings();
    }

    @GetMapping("/drivers-with-cars")
    public List<DriverWithCarDetails> getDriversWithAssignedCars() {
        return adminService.getDriversWithAssignedCars();
    }

    @GetMapping("/drivers-with-availability")
    public List<Driver> getDriversWithUpdatedAvailability() {
        return adminService.getDriversWithUpdatedAvailability();
    }

    @GetMapping("/available-cars")
    public List<Car> getAvailableCars() {
        return adminService.getAvailableCars();
    }

    @GetMapping("/available-drivers")
    public List<DriverWithCarDetails> getAvailableDrivers() {
        return adminService.getAvailableDriversWithCarDetails();
    }

    @GetMapping("/available-drivers-withoutCar")
    public List<Driver> getAvailableDriversWithout() {
        return adminService.getAvailableDrivers();
    }

    @PostMapping("/add-driver")
    public String addDriver(@RequestBody Driver driver) {
        return adminService.addDriver(driver);
    }

    @PutMapping("/accept-booking/{bookingId}")
    public String finalizeBooking(@PathVariable int bookingId) {
        return adminService.acceptBooking(bookingId);
    }

    @PostMapping("/add-car")
    public String addCar(@RequestBody Car car) {
        car.setAvailable(true);
        return adminService.addCar(car);
    }

    @PostMapping("/assign-car-to-driver")
    public String assignCarToDriver(@RequestParam int driverId, @RequestParam int carId) {
        return adminService.assignCarToDriver(driverId, carId);
    }

    @GetMapping("/bill/{bookingId}")
    public ResponseEntity<Bill> getBillByBookingId(@PathVariable int bookingId) {
        Bill bill = billService.getBillByBookingId(bookingId);
        return ResponseEntity.ok(bill);
    }

    @PutMapping("/price-config")
    public String updatePriceConfig(@RequestBody PriceConfig priceConfig) {
        return customerService.updatePriceConfig(priceConfig);
    }

    @GetMapping("/price-config")
    public PriceConfig getPriceConfig() {
        return customerService.getDefaultPriceConfig();
    }

    @PutMapping("/bill/update/{bookingId}")
    public String updateBill(
            @PathVariable int bookingId,
            @RequestBody BillUpdateRequest request) {
        return billService.updateBill(
                bookingId,
                request.getBaseFare(),
                request.getWaitingTimeCharge(),
                request.getTaxes(),
                request.getDiscount(),
                request.getTotalAmount()
        );
    }
}