package com.CabService.CabService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference // Prevents infinite recursion
    private Booking booking; // Foreign key to Booking

    private double baseFare; // Base fare for the ride
    private double waitingTimeCharge; // Extra charge for waiting time
    private double taxes; // Tax amount
    private double discount; // Discount amount
    private double totalAmount; // Total amount to be paid

    // Constructor to create a Bill with a Booking
    public Bill(Booking booking, double baseFare, double waitingTimeCharge, double taxes, double discount) {
        this.booking = booking;
        this.baseFare = baseFare;
        this.waitingTimeCharge = waitingTimeCharge;
        this.taxes = taxes;
        this.discount = discount;
        this.totalAmount = baseFare + waitingTimeCharge + taxes - discount; // Calculate total amount
    }
}