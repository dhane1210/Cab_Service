package com.CabService.CabService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int carId;
//
//    @Column(name = "is_available", nullable = false)
//    private boolean isAvailable;
//
//
//    private String model;
//    private String licensePlate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carId;

    private String model;
    private String licensePlate;

    private boolean isAvailable = true;


}
