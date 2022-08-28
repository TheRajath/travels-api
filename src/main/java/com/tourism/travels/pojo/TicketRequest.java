package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TicketRequest {

    @NotNull
    private Integer ticketId;

    @NotNull
    private Integer customerId;

    @NotNull
    private Integer packageId;

    @NotEmpty
    private String travelDate;

    @NotNull
    private Integer totalMembers;

    @NotNull
    private Integer totalCost;

}
