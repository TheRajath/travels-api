package com.tourism.travels.ticket;

import com.tourism.travels.sql.TicketEntity;
import com.tourism.travels.sql.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    private TicketService ticketService;

    @BeforeEach
    void setup() {

        ticketService = new TicketService(ticketRepository);
    }

    @Nested
    class GetTicketEntities {

        @Test
        void works() {
            // Arrange
            var ticketEntities = Collections.singletonList(new TicketEntity());

            when(ticketRepository.findAll()).thenReturn(ticketEntities);

            // Act
            var ticketDetails = ticketService.getTicketEntities();

            // Assert
            assertThat(ticketDetails).isEqualTo(ticketEntities);

            verify(ticketRepository).findAll();

            verifyNoMoreInteractions(ticketRepository);
        }

    }

}
