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
    @JsonBackReference
    private Booking booking;

    private double baseFare;
    private double waitingTimeCharge;
    private double taxes;
    private double discount;
    private double totalAmount;

    public Bill(Booking booking, double baseFare, double waitingTimeCharge, double taxes, double discount, double totalAmount) {
        this.booking = booking;
        this.baseFare = baseFare;
        this.waitingTimeCharge = waitingTimeCharge;
        this.taxes = taxes;
        this.discount = discount;
        this.totalAmount = totalAmount;
    }


}