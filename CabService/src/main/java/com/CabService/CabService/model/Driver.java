package com.CabService.CabService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int driverId;

    private String name;
    private String licenseNumber;
    private String phone;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;


    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car assignedCar; // Foreign key to Car


}
