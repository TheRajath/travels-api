package com.tourism.travels.controller;

import com.tourism.travels.exception.AlreadyExistsException;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerEntity signUp(CustomerEntity customerEntityWithUpdates) {

        var email = customerEntityWithUpdates.getEmail();

        customerRepository.findByEmail(email)
                .ifPresent(x -> { throw new AlreadyExistsException("Customer with this email: " + email + " already exists"); });

        return customerRepository.save(customerEntityWithUpdates);
    }

}
