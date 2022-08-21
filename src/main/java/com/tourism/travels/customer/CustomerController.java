package com.tourism.travels.customer;

import com.tourism.travels.pojo.CustomerSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final TravelMapper travelMapper;

    @PutMapping("/signup")
    public CustomerSignUp signUpCustomer(@Valid @RequestBody CustomerSignUp customerSignUp) {

        var customerEntity = travelMapper.toCustomerEntity(customerSignUp);
        var customerEntityWithUpdates = customerService.signUp(customerEntity);

        return travelMapper.toSignUpRequest(customerEntityWithUpdates);
    }

}
