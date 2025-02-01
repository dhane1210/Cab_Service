package com.CabService.CabService.model;

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
    private Booking booking;// Foreign key to Booking

    private double baseFare;
    private double waitingTimeCharge; // Extra charge for waiting time
    private double taxes;
    private double discount;
    private double totalAmount;

    public Bill(double baseFare, double taxes, double discount) {
        this.baseFare = baseFare;
        this.taxes = taxes;
        this.discount = discount;
        // Optionally calculate the total amount here, or set it later
        this.totalAmount = baseFare + taxes - discount;
    }
}
