package com.blibli.Customer.dto;
import lombok.Data;

@Data
public class CustomerRegistrationDTO {
    private String customerName;
    private String customerEmail;
    private String customerPassword;
    private String phoneNo;
    private String address;
}
