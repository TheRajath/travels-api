package com.tourism.travels.ticket;

import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.sql.TicketEntity;
import com.tourism.travels.sql.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Nested
    class CreateTicket {

        @Test
        void works() {
            // Arrange
            var ticketEntity = new TicketEntity();
            ticketEntity.setTicketId(890);

            // Act
            ticketService.createTicket(ticketEntity);

            // Assert
            verify(ticketRepository).findById(ticketEntity.getTicketId());
            verify(ticketRepository).save(ticketEntity);

            verifyNoMoreInteractions(ticketRepository);
        }

        @Test
        void throwsAlreadyExistsException_whenThereIsAnExistingRecord() {
            // Arrange
            var ticketEntity = new TicketEntity();
            ticketEntity.setTicketId(890);

            when(ticketRepository.findById(ticketEntity.getTicketId())).thenReturn(Optional.of(ticketEntity));

            // Act/Assert
            assertThatThrownBy(() -> ticketService.createTicket(ticketEntity))
                    .isInstanceOf(BusinessValidationException.class)
                    .hasMessage("Ticket already exists");
        }

        @Test
        void throwsAlreadyExistsException_whenThereIsARunTimeExceptionThrown() {
            // Arrange
            var ticketEntity = new TicketEntity();
            ticketEntity.setTicketId(890);

            when(ticketRepository.save(ticketEntity)).thenThrow(new RuntimeException("runtime exception"));

            // Act/Assert
            assertThatThrownBy(() -> ticketService.createTicket(ticketEntity))
                    .isInstanceOf(BusinessValidationException.class)
                    .hasMessage("The customerId/packageId is not a valid Id");
        }

    }

}
