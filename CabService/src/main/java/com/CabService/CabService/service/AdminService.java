package com.CabService.CabService.service;
import com.CabService.CabService.dto.DriverWithCarDetails;
import com.CabService.CabService.model.*;
import com.CabService.CabService.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
//    @Autowired
//    private PricingConfigRepository pricingConfigRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Update pricing
//    public String updatePricing(double discount, double tax) {
//        // Find the existing pricing configuration (assuming there is one)
//        PricingConfig config = pricingConfigRepository.findById(1).orElse(new PricingConfig());
//
//        // Update the pricing fields
//        config.setDiscount(discount);
//        config.setTax(tax);
//
//        // Save the updated pricing configuration
//        pricingConfigRepository.save(config);
//
//        return "Pricing updated successfully";
//    }

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
        driver.setAvailable(false);  // Mark driver as unavailable
        car.setAvailable(false);    // Mark car as unavailable

        // Save the updates
        driverRepository.save(driver);
        carRepository.save(car);

        return "Car assigned to driver successfully";
    }

    // Generate bill
    public Bill generateBill(double distance, double ratePerKm, double waitingTimeCharge, double taxRate, double discountRate) {
        double baseFare = distance * ratePerKm;
        double taxes = baseFare * (taxRate / 100);
        double discount = baseFare * (discountRate / 100);
        double totalAmount = baseFare + waitingTimeCharge + taxes - discount;

        Bill bill = new Bill();
        bill.setBaseFare(baseFare);
        bill.setWaitingTimeCharge(waitingTimeCharge);
        bill.setTaxes(taxes);
        bill.setDiscount(discount);
        bill.setTotalAmount(totalAmount);

        return bill;
    }



        // Calculate base fare
    public double calculateBaseFare(double distance) {
        return distance;
    }

    // Calculate tax
    public double calculateTax(double baseFare, double taxRate) {
        return baseFare * (taxRate / 100);
    }

//    public PricingConfig getPricingConfig() {
//        // Fetch the pricing configuration, assuming it's always the same one (ID = 1)
//        return pricingConfigRepository.findById(1).orElseThrow(() -> new RuntimeException("Pricing configuration not found"));
//    }

    // Calculate discount
    public double calculateDiscount(double baseFare, double discountRate) {
        return baseFare * (discountRate / 100);
    }
    // Generate bill
//    public Bill generateBill(double distance, double ratePerKm, double waitingTimeCharge, double taxRate, double discountRate) {
//        double baseFare = calculateBaseFare(distance);
//        double taxes = calculateTax(baseFare, taxRate);
//        double discount = calculateDiscount(baseFare, discountRate);
//        double totalAmount = baseFare + waitingTimeCharge + taxes - discount;
//
//        Bill bill = new Bill();
//        bill.setBaseFare(baseFare);
//        bill.setWaitingTimeCharge(waitingTimeCharge);
//        bill.setTaxes(taxes);
//        bill.setDiscount(discount);
//        bill.setTotalAmount(totalAmount);
//
//        return bill;
//    }





    public List<Driver> getDriversWithUpdatedAvailability() {
        // Fetch all drivers
        List<Driver> drivers = driverRepository.findAll();

        // Fetch drivers assigned to active bookings
        List<Driver> assignedDrivers = bookingRepository.findDriversAssignedToActiveBookings();

        // Update driver availability
        for (Driver driver : drivers) {
            boolean isAssigned = assignedDrivers.stream()
                    .anyMatch(assignedDriver -> assignedDriver.getDriverId() == driver.getDriverId());
            driver.setAvailable(!isAssigned);
        }

        return drivers;
    }







    public List<DriverWithCarDetails> getDriversWithAssignedCars() {
        return driverRepository.findDriversWithAssignedCars()
                .stream()
                .map(driver -> {
                    DriverWithCarDetails dto = new DriverWithCarDetails();
                    dto.setDriverId(driver.getDriverId());
                    dto.setName(driver.getName());
                    dto.setLicenseNumber(driver.getLicenseNumber());
                    dto.setPhone(driver.getPhone());
                    dto.setAvailable(driver.isAvailable());

                    // Since the query ensures assignedCar is not null, we don't need to check for null
                    dto.setCarModel(driver.getAssignedCar().getModel());
                    dto.setCarLicenseNumber(driver.getAssignedCar().getLicensePlate());

                    return dto;
                })
                .collect(Collectors.toList());
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

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll(); // Fetch all bookings from the database
    }

    public String acceptBooking(int bookingId) {
        // Find the booking by ID
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if the booking is already accepted
        if ("Accepted".equals(booking.getStatus())) {
            return "Booking is already accepted";
        }

        // Update the status to "Accepted"
        booking.setStatus("Accepted");
        bookingRepository.save(booking);

        return "Booking accepted successfully";
    }
}
