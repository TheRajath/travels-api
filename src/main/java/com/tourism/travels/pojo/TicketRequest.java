package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TicketRequest {

    @NotNull
    private int ticketId;

    @NotNull
    private int customerId;

    @NotNull
    private int packageId;

    @NotEmpty
    private String travelDate;

    @NotNull
    private int totalMembers;

    @NotNull
    private int totalCost;

}
