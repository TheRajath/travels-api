package com.tourism.travels.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageRequest {

    @NotNull
    private Integer packageId;

    @NotEmpty
    private String packageName;

    @NotEmpty
    private String tripDuration;

    @NotNull
    private Integer costPerPerson;

}
