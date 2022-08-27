package com.tourism.travels.customer;

import com.tourism.travels.exception.AlreadyExistsException;
import com.tourism.travels.exception.NotFoundException;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final TravelMapper travelMapper;
    private final CustomerRepository customerRepository;

    public List<CustomerEntity> getCustomerDetails() {

        return customerRepository.findAll();
    }

    public CustomerEntity getCustomerEntityById(int customerId) {

        return customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);
    }

    public CustomerEntity signUp(CustomerEntity newCustomerEntity) {

        var customerId = newCustomerEntity.getCustomerId();

        customerRepository.findById(customerId)
                .ifPresent(x -> { throw new AlreadyExistsException("Customer with this customerId: "
                           + customerId + " already exists"); });

        return customerRepository.save(newCustomerEntity);
    }

    public CustomerEntity updateCustomer(CustomerEntity customerEntityWithUpdates) {

        var customerId = customerEntityWithUpdates.getCustomerId();

        var customerEntity = customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);

        travelMapper.updateCustomerEntity(customerEntity, customerEntityWithUpdates);

        return customerRepository.save(customerEntityWithUpdates);
    }

    public void deleteByCustomerId(int customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);

        customerRepository.deleteById(customerId);
    }

}
