package com.blibli.Customer.services.impl;

import com.blibli.Customer.entity.CustomeUser;
import com.blibli.Customer.entity.Customer;
import com.blibli.Customer.repositories.CustomerRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Component
public class CustomerDetailService implements UserDetailsService {

    @Autowired
    private CustomerRepositories customerRepositories;

    @Override
    public UserDetails loadUserByUsername(String customerEmail) throws UsernameNotFoundException {
        Customer customer = customerRepositories.findByCustomerEmail(customerEmail);
        if (Objects.isNull(customer)) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomeUser(customer);
    }
}
