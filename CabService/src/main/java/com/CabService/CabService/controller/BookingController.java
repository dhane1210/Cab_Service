package com.CabService.CabService.controller;

import com.CabService.CabService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/generate-pdf/{bookingId}")
    public String generatePdf(@PathVariable int bookingId) {
        return bookingService.generateBookingPdf(bookingId);
    }
}
