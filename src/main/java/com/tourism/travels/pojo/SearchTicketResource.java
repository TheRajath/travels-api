package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchTicketResource {

    private String firstName;
    private String lastName;
    private String email;
    private String packageName;
    private String tripDuration;
    private LocalDate travelDate;
    private Integer totalMembers;
    private Integer totalCostOfTrip;

}
