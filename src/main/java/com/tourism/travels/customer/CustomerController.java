package com.tourism.travels.customer;

import com.tourism.travels.pojo.CustomerDetailsResource;
import com.tourism.travels.pojo.CustomerSignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final TravelMapper travelMapper;

    @GetMapping
    public List<CustomerDetailsResource> getCustomers() {

        return customerService.getCustomerDetails().stream()
                .map(travelMapper::toCustomerResource)
                .toList();
    }

    @GetMapping("/{customerId}")
    public CustomerDetailsResource getCustomerById(@PathVariable String customerId) {

        var customerEntity = customerService.getCustomerEntityById(Integer.parseInt(customerId));

        return travelMapper.toCustomerResource(customerEntity);
    }

    @PutMapping("/signup")
    public CustomerSignUp signUpCustomer(@Valid @RequestBody CustomerSignUp customerSignUp) {

        var customerEntity = travelMapper.toCustomerEntity(customerSignUp);
        var customerEntityWithUpdates = customerService.signUp(customerEntity);

        return travelMapper.toSignUpRequest(customerEntityWithUpdates);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCustomer(@PathVariable String customerId) {

        customerService.deleteByCustomerId(Integer.parseInt(customerId));
    }

}
