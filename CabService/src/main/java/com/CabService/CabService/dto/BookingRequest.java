package com.CabService.CabService.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private String startLocation;
    private String endLocation;
    private double distance;
    private double fare;
    private double waitingTime;
    private int customerId; // Direct customerId field
    private int driverId;   // Direct driverId field
}