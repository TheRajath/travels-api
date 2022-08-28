package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TicketResource {

    private int ticketId;
    private int customerId;
    private int packageId;
    private LocalDate travelDate;
    private int totalMembers;
    private int totalCost;

}
