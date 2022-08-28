package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class TicketRequest {

    @NotNull
    private Integer ticketId;

    @NotNull
    private Integer customerId;

    @NotNull
    private Integer packageId;

    @NotNull
    private LocalDate travelDate;

    @NotNull
    private Integer totalMembers;

    @NotNull
    private Integer totalCost;

}
