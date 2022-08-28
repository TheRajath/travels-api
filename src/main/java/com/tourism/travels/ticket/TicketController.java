package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.TicketResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TravelMapper travelMapper;
    private final TicketService ticketService;

    @GetMapping
    public List<TicketResource> getTickets() {

        return ticketService.getTicketEntities().stream()
                .map(travelMapper::toTicketResource)
                .toList();
    }

}
