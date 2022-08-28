package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.TicketRequest;
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
    public TicketRequest createTicket(@Valid @RequestBody TicketRequest ticketRequest) {

        var ticketEntity = travelMapper.toTicketEntity(ticketRequest);
        var newTicketEntity = ticketService.createTicket(ticketEntity);

        return travelMapper.toTicketRequest(newTicketEntity);

    }

}
