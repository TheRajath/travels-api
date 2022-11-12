package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.packages.PackageService;
import com.tourism.travels.pojo.*;
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
    private final PredicateBuilder predicateBuilder;

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

    @PostMapping("/search")
    public List<SearchTicketResource> searchTicket(@Valid @RequestBody SearchRequest searchRequest) {

        var predicate = predicateBuilder.buildSearchPredicate(searchRequest);

        return ticketService.getTicketsBySearchPredicate(predicate).stream()
                .map(travelMapper::mapSearchResource)
                .toList();
    }

    @PutMapping("/update")
    public TicketRequest updateTicket(@Valid @RequestBody TicketRequest ticketRequest) {

        var ticketEntity = travelMapper.toTicketEntity(ticketRequest);
        var newTicketEntityWithUpdates = ticketService.updateTicketById(ticketEntity);

        return travelMapper.toTicketRequest(newTicketEntityWithUpdates);
    }

    @DeleteMapping("/{ticketId}")
    public TicketRefund cancelTicket(@PathVariable Integer ticketId) {

        var refundAmount  = ticketService.deleteTicket(ticketId);

        return new TicketRefund(refundAmount);
    }

    private List<TicketResource> setTotalCostForTicketResources(List<TicketResource> ticketResources) {

        ticketResources.forEach(ticket -> {

            var packageId = Integer.parseInt(ticket.getPackageId());
            var totalMembers = Integer.parseInt(ticket.getTotalMembers());

            var packageEntity = packageService.getPackageEntityById(packageId);
            var costPerPerson = packageEntity.getCostPerPerson();
            var totalCost = totalMembers * costPerPerson;

            ticket.setTotalCost(totalCost);
        });

        return ticketResources;
    }

}
