package com.CabService.CabService.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false) // Maps the foreign key for the assigned driver
    private Driver assignedDriver;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // Foreign key to Customer

    private String startLocation;
    private String endLocation;
    private double distance; // In kilometers
    private double fare;
    private double waitingTime;

    private String status; // Pending, Confirmed, In Progress, Completed



}
