package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CustomerSignUp {

    @NotNull
    private Integer customerId;

    @NotEmpty
    private String firstName;

    private String lastName;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

}
