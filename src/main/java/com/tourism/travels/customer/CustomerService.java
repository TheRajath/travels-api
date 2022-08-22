package com.tourism.travels.customer;

import com.tourism.travels.exception.AlreadyExistsException;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerEntity> getCustomerDetails() {

        return customerRepository.findAll();
    }

    public CustomerEntity signUp(CustomerEntity customerEntityWithUpdates) {

        var customerId = customerEntityWithUpdates.getCustomerId();

        customerRepository.findById(customerId)
                .ifPresent(x -> { throw new AlreadyExistsException("Customer with this customerId: "
                           + customerId + " already exists"); });

        return customerRepository.save(customerEntityWithUpdates);
    }

}
