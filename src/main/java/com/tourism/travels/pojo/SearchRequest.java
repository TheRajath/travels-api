package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;
import com.tourism.travels.validation.NotEmptyIfPresent;

import javax.validation.constraints.Email;

public record SearchRequest(

    @NotEmptyIfPresent
    String customerId,

    @NotEmptyIfPresent
    String packageId,

    @Email
    @NotEmptyIfPresent
    String email,

    @DateFormatCheck
    String travelDate) {

}
