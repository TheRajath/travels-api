package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;

public record SearchRequest(

    Integer ticketId,
    Integer customerId,
    Integer packageId,

    @DateFormatCheck
    String travelDate) {

}
