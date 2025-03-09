package com.CabService.CabService.service;

import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.repo.BillRepository;
import com.CabService.CabService.repo.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingRepository bookingRepository;


    // Fetch a bill by booking ID
    public Bill getBillByBookingId(int bookingId) {
        return billRepository.findByBooking_BookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Bill not found for booking ID: " + bookingId));
    }

    @Transactional
    public String updateBill(int bookingId, double baseFare, double waitingTimeCharge, double taxes, double discount, double totalAmount) {
        // Fetch the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Fetch the bill associated with the booking
        Bill bill = billRepository.findByBooking_BookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Bill not found for the given booking"));

        // Update the bill details
        bill.setBaseFare(baseFare);
        bill.setWaitingTimeCharge(waitingTimeCharge);
        bill.setTaxes(taxes);
        bill.setDiscount(discount);
        bill.setTotalAmount(totalAmount);

        // Save the updated bill
        billRepository.save(bill);

        return "Bill updated successfully";
    }
}