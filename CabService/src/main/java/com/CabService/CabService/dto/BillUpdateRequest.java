package com.CabService.CabService.dto;


import lombok.Data;

@Data
public class BillUpdateRequest {
    private double baseFare;
    private double waitingTimeCharge;
    private double taxes;
    private double discount;
    private double totalAmount;


}