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

    private boolean isAvailable = true;

    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "carId", unique = true) // Add join column
    private Car assignedCar;
}
