package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TicketResource {

    private String ticketId;
    private String customerId;
    private String packageId;
    private LocalDate travelDate;
    private String totalMembers;
    private int totalCost;

}
