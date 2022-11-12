package com.tourism.travels.ticket;

import com.querydsl.core.types.Predicate;
import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.exception.NotFoundException;
import com.tourism.travels.sql.TicketEntity;
import com.tourism.travels.sql.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TravelMapper travelMapper;
    private final TicketRepository ticketRepository;

    public List<TicketEntity> getTicketEntities() {

        return ticketRepository.findAll();
    }

    public TicketEntity createTicket(TicketEntity ticketEntity) {

        var ticketId = ticketEntity.getTicketId();

        ticketRepository.findById(ticketId)
                .ifPresent(x -> { throw new BusinessValidationException("Ticket already exists"); });

        try {

            return ticketRepository.save(ticketEntity);
        }
        catch (RuntimeException exception) {

            throw new BusinessValidationException("The customerId/packageId is not a valid Id");
        }

    }

    public Page<TicketEntity> getTicketsBySearchPredicate(Predicate predicate, Pageable pageable) {

        return ticketRepository.findAll(predicate, pageable);
    }

    public TicketEntity updateTicketById(TicketEntity ticketEntityWithUpdates) {

        var ticketId = ticketEntityWithUpdates.getTicketId();

        var ticketEntity = ticketRepository.findById(ticketId)
                .orElseThrow(NotFoundException::new);

        travelMapper.updateTicketEntity(ticketEntity, ticketEntityWithUpdates);

        try {

            return ticketRepository.save(ticketEntity);
        }
        catch (RuntimeException exception) {

            throw new BusinessValidationException("The customerId/packageId is not a valid Id");
        }

    }

    public Integer deleteTicket(int ticketId) {

        var ticketEntity = ticketRepository.findById(ticketId)
                .orElseThrow(NotFoundException::new);

        var costPerPerson = ticketEntity.getPackageEntity().getCostPerPerson();
        var totalMembers = ticketEntity.getTotalMembers();
        var totalCost = totalMembers * costPerPerson;

        ticketRepository.deleteById(ticketId);

        return (totalCost * 80) / 100;
    }

}
