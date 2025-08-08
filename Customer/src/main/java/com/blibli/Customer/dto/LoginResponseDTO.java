package com.blibli.Customer.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private int customerId;
    private String customerName;
    @Column(nullable = false, unique = true)
    private String customerEmail;
    private String customerPassword;
    private String phoneNo;
    private String address;
    private String token;
}
