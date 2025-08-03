package com.blibli.Customer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int customerId;

    private String customerName;
    @Column(nullable = false, unique = true)
    private String customerEmail;
    private String customerPassword;
    private String phoneNo;
    private String address;
}
