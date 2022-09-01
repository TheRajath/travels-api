package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {

    private Integer ticketId;
    private Integer customerId;
    private Integer packageId;

    @DateFormatCheck
    private String travelDate;

}
