package com.CabService.CabService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the pricing configuration

    private double baseFare; // The base fare for any ride

    private double distanceRate; // Fare per kilometer/mile

    private double waitingRate; // Fare per minute of waiting time

    private double tax; // Tax percentage (e.g., 10%)

    private double discount; // Discount percentage (e.g., 5%)


//    public PricingConfig(double discount, double tax) {
//        this.discount = discount;
//        this.tax = tax;
//    }
}
