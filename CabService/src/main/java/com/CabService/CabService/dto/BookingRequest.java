package com.CabService.CabService.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private int driverId;
    private int customerId;
    private String startLocation;
    private String endLocation;
    private double distance;
    private double fare;

    // Getters and setters
}