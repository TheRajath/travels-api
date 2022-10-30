package com.tourism.travels.ticket;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.packages.PackageService;
import com.tourism.travels.pojo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.tourism.travels.sql.QTicketEntity.ticketEntity;

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

    @PostMapping("/search")
    public List<SearchTicketResource> searchTicket(@Valid @RequestBody SearchRequest searchRequest) {

        var predicate = buildSearchPredicate(searchRequest);

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

    private Predicate buildSearchPredicate(SearchRequest searchRequest) {

        var predicate = new BooleanBuilder();

        if (searchRequest.customerId() != null) {

            predicate.and(ticketEntity.customerId.eq(searchRequest.customerId()));
        }

        if (searchRequest.packageId() != null) {

            predicate.and(ticketEntity.packageId.eq(searchRequest.packageId()));
        }

        if (searchRequest.email() != null) {

            predicate.and(ticketEntity.customerEntity.email.eq(searchRequest.email()));
        }

        if (searchRequest.travelDate() != null) {

            predicate.and(ticketEntity.travelDate.eq(LocalDate.parse(searchRequest.travelDate())));
        }

        if (predicate.getValue() == null) {

            throw new BusinessValidationException("request body must contain at least one of the following search" +
                    " criteria: customerId, packageId, email, travelDate");
        }

        return predicate;
    }

}
