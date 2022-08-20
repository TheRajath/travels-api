package com.tourism.travels.controller;

import com.tourism.travels.exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelsService {

    private final PackageRepository packageRepository;
    private final CustomerRepository customerRepository;

    public List<PackageEntity> getPackageDetails() {

        return packageRepository.findAll();
    }

    public CustomerEntity signUp(CustomerEntity customerEntityWithUpdates) {

        var email = customerEntityWithUpdates.getEmail();

        customerRepository.findByEmail(email)
                .ifPresent(x -> { throw new AlreadyExistsException("Customer with this email: " + email + " already exists"); });

        return customerRepository.save(customerEntityWithUpdates);
    }

}
