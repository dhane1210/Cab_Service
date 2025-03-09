package com.CabService.CabService.repo;

import com.CabService.CabService.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    @Query("SELECT d FROM Driver d WHERE d.isAvailable = true")
    List<Driver> findAvailableDrivers();

    // Find all drivers with assigned cars
    @Query("SELECT d FROM Driver d WHERE d.assignedCar IS NOT NULL")
    List<Driver> findDriversWithAssignedCars();
}
