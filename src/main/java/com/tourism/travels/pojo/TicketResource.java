package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResource {

    private int ticketId;
    private int customerId;
    private int packageId;
    private String travelDate;
    private int totalMembers;
    private int totalCost;

}
