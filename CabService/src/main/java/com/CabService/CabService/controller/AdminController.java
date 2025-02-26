package com.CabService.CabService.controller;

import com.CabService.CabService.dto.DriverWithCarDetails;
import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.model.Car;
import com.CabService.CabService.model.Driver;
import com.CabService.CabService.service.AdminService;
import com.CabService.CabService.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BillService billService;
    @Autowired
    private AdminService adminService;


    // Update discounts and taxes
    @PutMapping("/update-pricing")
    public String updatePricing(@RequestParam double discount, @RequestParam double tax) {
        return adminService.updatePricing(discount, tax);
    }

    @GetMapping("/all-bookings")
    public List<Booking> getAllBookings() {
        return adminService.getAllBookings();
    }

    @GetMapping("/drivers-with-cars")
    public List<DriverWithCarDetails> getDriversWithAssignedCars() {
        return adminService.getDriversWithAssignedCars();
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



    // Manage drivers
    @PostMapping("/add-driver")
    public String addDriver(@RequestBody Driver driver) {
        return adminService.addDriver(driver);
    }

    @PutMapping("/accept-booking/{bookingId}")
    public String finalizeBooking(@PathVariable int bookingId) {
        return adminService.acceptBooking(bookingId);
    }
    // Manage cars
    @PostMapping("/add-car")
    public String addCar(@RequestBody Car car) {
        car.setAvailable(true);
        return adminService.addCar(car);
    }

    // Connect car to driver
    @PostMapping("/assign-car-to-driver")
    public String assignCarToDriver(@RequestParam int driverId, @RequestParam int carId) {
        return adminService.assignCarToDriver(driverId, carId);
    }

    // Create a new bill
    @PostMapping("/create-bill")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill createdBill = billService.createBill(bill);
        return ResponseEntity.ok(createdBill);
    }

    // Fetch a bill by booking ID
    @GetMapping("/bill/{bookingId}")
    public ResponseEntity<Bill> getBillByBookingId(@PathVariable int bookingId) {
        Bill bill = billService.getBillByBookingId(bookingId);
        return ResponseEntity.ok(bill);
    }
}