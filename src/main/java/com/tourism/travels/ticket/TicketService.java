package com.tourism.travels.ticket;

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

}
