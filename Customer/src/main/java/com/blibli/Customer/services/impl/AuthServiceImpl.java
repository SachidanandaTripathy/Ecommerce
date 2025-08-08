package com.blibli.Customer.services.impl;
import com.blibli.Customer.dto.CustomerRegistrationDTO;
import com.blibli.Customer.dto.LoginResponseDTO;
import com.blibli.Customer.entity.Customer;
import com.blibli.Customer.exception.CustomException;
import com.blibli.Customer.repositories.CustomerRepositories;
import com.blibli.Customer.services.AuthServices;
import com.blibli.Customer.services.JwtServices;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthServices {
    @Autowired
    private CustomerRepositories customerRepositories;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtServices jwtService;

    @Override
    public String saveUser(CustomerRegistrationDTO customerRegistrationDTO) {
        Customer existingCustomer = customerRepositories.findByCustomerEmail(customerRegistrationDTO.getCustomerEmail());

        if (existingCustomer != null) {
            throw new CustomException("Customer with this email already exists.");
        }

        Customer newCustomer = new Customer();
        BeanUtils.copyProperties(customerRegistrationDTO, newCustomer);
        newCustomer.setCustomerPassword(passwordEncoder.encode(customerRegistrationDTO.getCustomerPassword()));

        customerRepositories.save(newCustomer);
        return "User added to the system";
    }

    public LoginResponseDTO generateToken(String customerEmail) {
        String token=jwtService.generateToken(customerEmail);
        LoginResponseDTO loginResponseDTO=new LoginResponseDTO();
        Customer customer=customerRepositories.findByCustomerEmail(customerEmail);
        loginResponseDTO.setToken(token);
        BeanUtils.copyProperties(customer,loginResponseDTO);
        return loginResponseDTO;
    }
    @Override
    public LoginResponseDTO getUserByEmail(String email) {
        Customer customer=customerRepositories.findByCustomerEmail(email);
        LoginResponseDTO loginResponseDTO=new LoginResponseDTO();
        BeanUtils.copyProperties(customer,loginResponseDTO);
        return loginResponseDTO;
    }

    @Override
    public String validateToken(String token) {
        return jwtService.validateToken(token);
    }

}
