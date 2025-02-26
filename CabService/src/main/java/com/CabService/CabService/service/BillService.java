package com.CabService.CabService.service;

import com.CabService.CabService.model.Bill;
import com.CabService.CabService.model.Booking;
import com.CabService.CabService.repo.BillRepository;
import com.CabService.CabService.repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Bill createBill(Bill bill) {
        if (bill.getBooking() == null || bill.getBooking().getBookingId() == 0) {
            throw new RuntimeException("Booking information is missing in the Bill object.");
        }

        Booking booking = bookingRepository.findById(bill.getBooking().getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        bill.setBooking(booking);

        bill.setTotalAmount(bill.getBaseFare() + bill.getWaitingTimeCharge() + bill.getTaxes() - bill.getDiscount());

        return billRepository.save(bill);
    }


    // Fetch a bill by booking ID
    public Bill getBillByBookingId(int bookingId) {
        return billRepository.findByBooking_BookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Bill not found for booking ID: " + bookingId));
    }
}