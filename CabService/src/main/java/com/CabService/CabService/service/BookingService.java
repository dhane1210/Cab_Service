package com.CabService.CabService.service;

import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.repo.BookingRepository;
import com.CabService.CabService.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AdminService adminService;

    // Calculate fare
    public double calculateFare(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        double baseFare = adminService.calculateBaseFare(booking.getDistance());

        // Get the tax rate from the pricing configuration
        double taxRate = adminService.getPricingConfig().getTax();  // Assuming getTax() returns the tax rate

        double tax = adminService.calculateTax(baseFare, taxRate);  // Pass both baseFare and taxRate
        double discountRate = adminService.getPricingConfig().getDiscount();  // Assuming getDiscount() returns the discount rate
        double discount = adminService.calculateDiscount(baseFare, discountRate);

        return baseFare + tax - discount;
    }

    // Generate booking PDF
    public String generateBookingPdf(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        // Logic to generate a PDF
        return "Booking PDF generated successfully";
    }
}
