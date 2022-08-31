package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchRequest {

    private Integer ticketId;
    private Integer customerId;
    private Integer packageId;
    private LocalDate travelDate;

}
