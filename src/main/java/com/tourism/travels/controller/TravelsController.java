package com.tourism.travels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelsController {

    private final TravelsService travelsService;
    private final TravelMapper travelMapper;

    @GetMapping("/packages")
    public List<PackageDetailsResource> getPackages() {

        return travelsService.getPackageDetails().stream()
                .map(travelMapper::toPackageDetailsResource)
                .toList();
    }

    @PutMapping("/customer/signup")
    public CustomerSignUp signUpCustomer(@Valid @RequestBody CustomerSignUp customerSignUp) {

        var customerEntity = travelMapper.toCustomerEntity(customerSignUp);
        var customerEntityWithUpdates = travelsService.signUp(customerEntity);

        return travelMapper.toSignUpRequest(customerEntityWithUpdates);
    }

}
