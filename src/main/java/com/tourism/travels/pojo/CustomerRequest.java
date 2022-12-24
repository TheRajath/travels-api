package com.tourism.travels.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

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
