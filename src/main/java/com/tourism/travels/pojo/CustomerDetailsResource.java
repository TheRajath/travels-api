package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDetailsResource {

    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
