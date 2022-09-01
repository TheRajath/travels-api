package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
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
    @FutureOrPresent
    private LocalDate travelDate;

    @NotNull
    private Integer totalMembers;

}
