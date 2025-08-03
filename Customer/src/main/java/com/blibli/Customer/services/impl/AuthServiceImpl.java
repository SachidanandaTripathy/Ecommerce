package com.blibli.Customer.services.impl;
import com.blibli.Customer.dto.CustomerRegistrationDTO;
import com.blibli.Customer.entity.Customer;
import com.blibli.Customer.repositories.CustomerRepositories;
import com.blibli.Customer.services.AuthServices;
import com.blibli.Customer.services.JwtServices;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthServices {
    @Autowired
    private CustomerRepositories customerRepositories;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtServices jwtService;

    public String saveUser(CustomerRegistrationDTO customerRegistrationDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerRegistrationDTO, customer);
        customer.setCustomerPassword(passwordEncoder.encode(customerRegistrationDTO.getCustomerPassword()));
        customerRepositories.save(customer);
        return "user added to the system";
    }

    public String generateToken(String customerEmail) {
        return jwtService.generateToken(customerEmail);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
