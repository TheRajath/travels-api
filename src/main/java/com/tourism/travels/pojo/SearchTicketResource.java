package com.tourism.travels.pojo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SearchTicketResource {

    private Pagination pagination;
    private List<TicketDetail> ticketDetails;

    @Getter
    @Setter
    public static class TicketDetail {

        private String firstName;
        private String lastName;
        private String email;
        private String packageName;
        private String tripDuration;
        private LocalDate travelDate;
        private Integer totalMembers;
        private Integer totalCostOfTrip;

    }

}
