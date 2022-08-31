package com.tourism.travels.ticket;

import com.tourism.travels.exception.BusinessValidationException;
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

}
