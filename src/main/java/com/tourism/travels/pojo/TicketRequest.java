package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;
import com.tourism.travels.validation.PresentOrFutureDateCheck;
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
    @DateFormatCheck
    @PresentOrFutureDateCheck
    private String travelDate;

    @NotNull
    private Integer totalMembers;

}
