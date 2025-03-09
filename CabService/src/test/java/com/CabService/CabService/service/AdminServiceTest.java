package com.CabService.CabService.service;

import com.CabService.CabService.model.Car;
import com.CabService.CabService.repo.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceTest {

    @Mock
    private CarRepository carRepository; // Mock the CarRepository

    @InjectMocks
    private AdminService adminService; // Inject the mocked dependencies into AdminService

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void addCar() {
        // Arrange
        Car car = new Car(); // Create a Car object (this simulates the data coming from the frontend)
        car.setModel("Toyota Camry"); // Set model (this is just an example)
        car.setLicensePlate("ABC123"); // Set license plate (this is just an example)
        car.setAvailable(true); // Set availability (this is just an example)

        // Mock the behavior of carRepository.save()
        when(carRepository.save(car)).thenReturn(car);

        // Act
        String result = adminService.addCar(car);

        // Assert
        assertEquals("Car added successfully", result); // Verify the result
        verify(carRepository, times(1)).save(car); // Verify that carRepository.save() was called once
    }

}