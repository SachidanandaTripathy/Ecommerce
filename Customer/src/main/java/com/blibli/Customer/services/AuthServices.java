package com.blibli.Customer.services;
import com.blibli.Customer.dto.CustomerRegistrationDTO;
import com.blibli.Customer.dto.LoginResponseDTO;


public interface AuthServices {
    String saveUser(CustomerRegistrationDTO customerRegistrationDTO);
    LoginResponseDTO generateToken(String customerEmail);

    LoginResponseDTO getUserByEmail(String email);

    String validateToken(String token);
}
