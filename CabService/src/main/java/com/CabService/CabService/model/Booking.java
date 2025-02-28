package com.CabService.CabService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    private String startLocation;
    private String endLocation;
    private double distance;
    private double fare;
    private String status;
    private double waitingTime; // Add this field
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonManagedReference // Prevents infinite recursion
    private Bill bill; // Reference to the Bill entity

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver assignedDriver;
}