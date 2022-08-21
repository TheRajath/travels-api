package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddPackageRequest {

    @NotNull
    private Integer packageId;

    @NotEmpty
    private String packageName;

    @NotEmpty
    private String tripDuration;

    @NotNull
    private Integer costPerPerson;

}
