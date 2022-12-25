package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.pojo.*;
import com.tourism.travels.sql.TicketEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TravelMapper travelMapper;
    private final TicketService ticketService;
    private final PredicateBuilder predicateBuilder;

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

    @PostMapping("/search")
    public SearchTicketResource searchTicket(@Valid @RequestBody SearchRequest searchRequest) {

        var predicate = predicateBuilder.buildSearchPredicate(searchRequest);

        var pagination = searchRequest.getPagination();
        var sortResultsBy = searchRequest.getSortResultsBy();
        var pageRequest = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize())
                .withSort(Sort.by(sortResultsBy.getOrderBy(), sortResultsBy.getFieldName().getColumnName()));

        var ticketEntityPage = ticketService.getTicketsBySearchPredicate(predicate, pageRequest);

        var resultPagination = buildPaginationForTicketSearch(ticketEntityPage);

        var ticketDetails = ticketEntityPage.stream()
                .map(travelMapper::mapTicketDetails)
                .toList();

        var searchTicketResource = new SearchTicketResource();
        searchTicketResource.setPagination(resultPagination);
        searchTicketResource.setTicketDetails(ticketDetails);

        return searchTicketResource;
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

    private Pagination buildPaginationForTicketSearch(Page<TicketEntity> ticketEntityPage) {

        var pagination = new Pagination();

        pagination.setPageNumber(ticketEntityPage.getNumber());
        pagination.setPageSize(ticketEntityPage.getSize());
        pagination.setTotalReturnCount(ticketEntityPage.getTotalElements());
        pagination.setTotalRowCount(ticketEntityPage.getNumberOfElements());

        return pagination;
    }

}
