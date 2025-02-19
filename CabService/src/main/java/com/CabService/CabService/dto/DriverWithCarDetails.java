package com.CabService.CabService.dto;

import lombok.Data;

@Data
public class DriverWithCarDetails {
    private int driverId;
    private String name;
    private String licenseNumber;
    private String phone;
    private boolean isAvailable;
    private String carModel;
    private String carLicenseNumber;


}
