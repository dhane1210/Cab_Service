package com.CabService.CabService.repo;

import com.CabService.CabService.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    // Custom query to find a bill by booking ID
    Optional<Bill> findByBooking_BookingId(int bookingId);
}