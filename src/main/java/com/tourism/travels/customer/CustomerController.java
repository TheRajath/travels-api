package com.tourism.travels.customer;

import com.tourism.travels.pojo.CustomerRequest;
import com.tourism.travels.pojo.CustomerResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final TravelMapper travelMapper;
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResource> getCustomers() {

        return customerService.getCustomerDetails().stream()
                .map(travelMapper::toCustomerResource)
                .toList();
    }

    @GetMapping("/{customerId}")
    public CustomerResource getCustomerById(@PathVariable String customerId) {

        var customerEntity = customerService.getCustomerEntityById(Integer.parseInt(customerId));

        return travelMapper.toCustomerResource(customerEntity);
    }

    @PutMapping("/signup")
    public CustomerRequest signUpCustomer(@Valid @RequestBody CustomerRequest customerRequest) {

        var customerEntity = travelMapper.toCustomerEntity(customerRequest);
        var customerEntityWithUpdates = customerService.signUp(customerEntity);

        return travelMapper.toCustomerRequest(customerEntityWithUpdates);
    }

    @PutMapping("/update")
    public CustomerRequest updateCustomerDetails(@Valid @RequestBody CustomerRequest customerRequest) {

        var customerEntity = travelMapper.toCustomerEntity(customerRequest);
        var customerEntityWithUpdates = customerService.updateCustomer(customerEntity);

        return travelMapper.toCustomerRequest(customerEntityWithUpdates);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCustomer(@PathVariable String customerId) {

        customerService.deleteByCustomerId(Integer.parseInt(customerId));
    }

}
