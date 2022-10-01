package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;

public record SearchRequest(

    String customerId,
    String packageId,

    @DateFormatCheck
    String travelDate) {

}
