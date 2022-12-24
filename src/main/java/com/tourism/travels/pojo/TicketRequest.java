package com.tourism.travels.pojo;

import com.tourism.travels.validation.PresentOrFutureDateCheck;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

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
