package com.CabService.CabService.service;
import com.CabService.CabService.dto.DriverWithCarDetails;
import com.CabService.CabService.model.*;
import com.CabService.CabService.repo.AdminRepository;
import com.CabService.CabService.repo.CarRepository;
import com.CabService.CabService.repo.DriverRepository;
import com.CabService.CabService.repo.PricingConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PricingConfigRepository pricingConfigRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private DriverRepository driverRepository;

    // Authenticate admin
    public String authenticate(String username, String password) {
        Admin admin = adminRepository.findByUsernameAndPassword(username, password);
        return (admin != null) ? "Login successful" : "Invalid credentials";
    }

    // Update pricing
    public String updatePricing(double discount, double tax) {
        // Find the existing pricing configuration (assuming there is one)
        PricingConfig config = pricingConfigRepository.findById(1).orElse(new PricingConfig());

        // Update the pricing fields
        config.setDiscount(discount);
        config.setTax(tax);

        // Save the updated pricing configuration
        pricingConfigRepository.save(config);

        return "Pricing updated successfully";
    }

    // Manage cars
    public String addCar(Car car) {
        carRepository.save(car);
        return "Car added successfully";
    }

    public List<Car> getAvailableCars() {
        return carRepository.findAvailableCars();
    }

    // Manage drivers
    public String addDriver(Driver driver) {
        driverRepository.save(driver);
        return "Driver added successfully";
    }

    public List<Driver> getAvailableDrivers() {
        return driverRepository.findAvailableDrivers();
    }


    // Assign car to driver
    public String assignCarToDriver(int driverId, int carId) {
        // Fetch the driver
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Fetch the car
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        // Ensure the car is available
        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available for assignment");
        }

        // Update driver and car details
        driver.setAssignedCar(car); // Set car for driver
        driver.setAvailable(true);  // Mark driver as available
        car.setAvailable(false);    // Mark car as unavailable

        // Save the updates
        driverRepository.save(driver);
        carRepository.save(car);

        return "Car assigned to driver successfully";
    }



    // Calculate base fare
    public double calculateBaseFare(double distance) {
        return distance;
    }

    // Calculate tax
    public double calculateTax(double baseFare, double taxRate) {
        return baseFare * (taxRate / 100);
    }

    public PricingConfig getPricingConfig() {
        // Fetch the pricing configuration, assuming it's always the same one (ID = 1)
        return pricingConfigRepository.findById(1).orElseThrow(() -> new RuntimeException("Pricing configuration not found"));
    }

    // Calculate discount
    public double calculateDiscount(double baseFare, double discountRate) {
        return baseFare * (discountRate / 100);
    }
    // Generate bill
    public Bill generateBill(double distance, double ratePerKm, double waitingTimeCharge, double taxRate, double discountRate) {
        double baseFare = calculateBaseFare(distance);
        double taxes = calculateTax(baseFare, taxRate);
        double discount = calculateDiscount(baseFare, discountRate);
        double totalAmount = baseFare + waitingTimeCharge + taxes - discount;

        Bill bill = new Bill();
        bill.setBaseFare(baseFare);
        bill.setWaitingTimeCharge(waitingTimeCharge);
        bill.setTaxes(taxes);
        bill.setDiscount(discount);
        bill.setTotalAmount(totalAmount);

        return bill;
    }

    public List<DriverWithCarDetails> getAvailableDriversWithCarDetails() {
        return driverRepository.findAvailableDrivers()
                .stream()
                .map(driver -> {
                    DriverWithCarDetails dto = new DriverWithCarDetails();
                    dto.setDriverId(driver.getDriverId());
                    dto.setName(driver.getName());
                    dto.setLicenseNumber(driver.getLicenseNumber());
                    dto.setPhone(driver.getPhone());
                    dto.setAvailable(driver.isAvailable());

                    if (driver.getAssignedCar() != null) {
                        dto.setCarModel(driver.getAssignedCar().getModel());
                        dto.setCarLicenseNumber(driver.getAssignedCar().getLicensePlate());
                    } else {
                        dto.setCarModel("No car assigned");
                        dto.setCarLicenseNumber("N/A");
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
