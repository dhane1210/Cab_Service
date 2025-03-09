package com.CabService.CabService.repo;

import com.CabService.CabService.model.Booking;
import com.CabService.CabService.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Find bookings by customer ID
    List<Booking> findByCustomer_CustomerId(int customerId);

    // Find active bookings for a specific customer
    @Query("SELECT b FROM Booking b WHERE b.customer.customerId = :customerId AND b.status = 'Active'")
    Booking findActiveBookingByCustomer(@Param("customerId") int customerId);

    // Find assigned booking for a specific driver
    @Query("SELECT b FROM Booking b WHERE b.assignedDriver.driverId = :driverId AND b.status = 'Assigned'")
    Booking findAssignedBookingByDriver(@Param("driverId") int driverId);

    List<Booking> findAll();

    Booking findByBookingId(int bookingId);
    // Find all drivers assigned to active bookings
    @Query("SELECT DISTINCT b.assignedDriver FROM Booking b WHERE b.status = 'Active'")
    List<Driver> findDriversAssignedToActiveBookings();
}
