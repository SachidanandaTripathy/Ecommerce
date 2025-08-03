package com.blibli.Customer.repositories;
import com.blibli.Customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepositories extends JpaRepository<Customer,Integer> {
    Customer findByCustomerEmail(String username);
}
