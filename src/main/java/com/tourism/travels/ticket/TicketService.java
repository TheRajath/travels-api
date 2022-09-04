package com.tourism.travels.ticket;

import com.querydsl.core.types.Predicate;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.exception.NotFoundException;
import com.tourism.travels.sql.TicketEntity;
import com.tourism.travels.sql.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

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

    public List<TicketEntity> getTicketsBySearchPredicate(Predicate predicate) {

        return (List<TicketEntity>) ticketRepository.findAll(predicate);
    }

    public void deleteTicket(int ticketId) {

        ticketRepository.findById(ticketId)
                .orElseThrow(NotFoundException::new);

        ticketRepository.deleteById(ticketId);
    }

}
