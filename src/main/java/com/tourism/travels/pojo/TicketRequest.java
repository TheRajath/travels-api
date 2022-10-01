package com.tourism.travels.pojo;

import com.tourism.travels.validation.PresentOrFutureDateCheck;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class TicketRequest {

    @NotEmpty
    private String ticketId;

    @NotEmpty
    private String customerId;

    @NotEmpty
    private String packageId;

    @NotEmpty
    @PresentOrFutureDateCheck
    private String travelDate;

    @NotEmpty
    private String totalMembers;

}
