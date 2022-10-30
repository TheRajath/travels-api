package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;
import com.tourism.travels.validation.NotEmptyIfPresent;

public record SearchRequest(

    @NotEmptyIfPresent
    String customerId,

    @NotEmptyIfPresent
    String packageId,

    @DateFormatCheck
    String travelDate) {

}
