package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.TicketResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PutMapping("/create")
    public TicketResource createTicket(@Valid @RequestBody TicketResource ticketResource) {

        var ticketEntity = travelMapper.toTicketEntity(ticketResource);
        var newTicketEntity = ticketService.createTicket(ticketEntity);

        return travelMapper.toTicketResource(newTicketEntity);

    }

}
