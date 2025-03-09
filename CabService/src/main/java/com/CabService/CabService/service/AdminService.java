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
    private CarRepository carRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public String addCar(Car car) {
        carRepository.save(car);
        return "Car added successfully";
    }

    public List<Car> getAvailableCars() {
        return carRepository.findAvailableCars();
    }


    public String addDriver(Driver driver) {

        driverRepository.save(driver);
        return "Driver added successfully";
    }

    public List<Driver> getAvailableDrivers() {
        return driverRepository.findAvailableDrivers();
    }



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


        driver.setAssignedCar(car); // Set car for driver
        driver.setAvailable(false);  // Mark driver as unavailable
        car.setAvailable(false);    // Mark car as unavailable

        // Save the updates
        driverRepository.save(driver);
        carRepository.save(car);

        return "Car assigned to driver successfully";
    }


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
