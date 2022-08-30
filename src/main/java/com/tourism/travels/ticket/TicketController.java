package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.packages.PackageService;
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
    private final PackageService packageService;

    @GetMapping
    public List<TicketResource> getTickets() {

        var ticketResources = ticketService.getTicketEntities().stream()
                .map(travelMapper::toTicketResource)
                .toList();

        return setTotalCostForTicketResources(ticketResources);
    }

    @PutMapping("/create")
    public TicketRequest createTicket(@Valid @RequestBody TicketRequest ticketRequest) {

        var ticketEntity = travelMapper.toTicketEntity(ticketRequest);
        var newTicketEntity = ticketService.createTicket(ticketEntity);

        return travelMapper.toTicketRequest(newTicketEntity);

    }

    private List<TicketResource> setTotalCostForTicketResources(List<TicketResource> ticketResources) {

        ticketResources.forEach(ticket -> {

            var packageId = ticket.getPackageId();
            var totalMembers = ticket.getTotalMembers();

            var packageEntity = packageService.getPackageEntityById(packageId);
            var costPerPerson = packageEntity.getCostPerPerson();
            var totalCost = totalMembers * costPerPerson;

            ticket.setTotalCost(totalCost);
        });

        return ticketResources;
    }

}
