package com.CabService.CabService.repo;

import com.CabService.CabService.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    // Find all available cars
    @Query("SELECT c FROM Car c WHERE c.isAvailable = true")
    List<Car> findAvailableCars();
}
