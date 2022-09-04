package com.tourism.travels.ticket;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.packages.PackageService;
import com.tourism.travels.pojo.SearchRequest;
import com.tourism.travels.pojo.SearchTicketResource;
import com.tourism.travels.pojo.TicketRequest;
import com.tourism.travels.pojo.TicketResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.tourism.travels.sql.QTicketEntity.ticketEntity;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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

    @DeleteMapping("/{ticketId}")
    @ResponseStatus(NO_CONTENT)
    public void cancelTicket(@PathVariable Integer ticketId) {

        ticketService.deleteTicket(ticketId);
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

    private Predicate buildSearchPredicate(SearchRequest searchRequest) {

        var predicate = new BooleanBuilder();

        if (searchRequest.getTicketId() != null) {

            predicate.and(ticketEntity.ticketId.eq(searchRequest.getTicketId()));
        }

        if (searchRequest.getCustomerId() != null) {

            predicate.and(ticketEntity.customerId.eq(searchRequest.getCustomerId()));
        }

        if (searchRequest.getPackageId() != null) {

            predicate.and(ticketEntity.packageId.eq(searchRequest.getPackageId()));
        }

        if (searchRequest.getTravelDate() != null) {

            predicate.and(ticketEntity.travelDate.eq(LocalDate.parse(searchRequest.getTravelDate())));
        }

        if (predicate.getValue() == null) {

            throw new BusinessValidationException("request body must contain at least one of the following search" +
                    " criteria: ticketId, customerId, packageId, travelDate");
        }

        return predicate;
    }

}
