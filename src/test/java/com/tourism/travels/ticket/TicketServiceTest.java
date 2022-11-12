package com.tourism.travels.ticket;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.exception.NotFoundException;
import com.tourism.travels.sql.PackageEntity;
import com.tourism.travels.sql.TicketEntity;
import com.tourism.travels.sql.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TravelMapper travelMapper;

    @Mock
    private TicketRepository ticketRepository;

    private TicketService ticketService;

    @BeforeEach
    void setup() {

        ticketService = new TicketService(travelMapper, ticketRepository);
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

    @Nested
    class GetTicketsBySearchPredicate {

        @Test
        void works() {
            // Arrange
            Predicate predicate = new BooleanBuilder();
            var ticketEntity = new TicketEntity();
            var pageRequest = PageRequest.of(0, 25);

            PageImpl<TicketEntity> ticketEntities =
                    new PageImpl<>(Collections.singletonList(ticketEntity), pageRequest, 20);

            when(ticketRepository.findAll(predicate, pageRequest)).thenReturn(ticketEntities);

            // Act
            var retrievedTicketEntities = ticketService.getTicketsBySearchPredicate(predicate, pageRequest);

            // Assert
            assertThat(retrievedTicketEntities).isNotNull().isNotEmpty().isEqualTo(ticketEntities);

            verify(ticketRepository).findAll(predicate, pageRequest);

            verifyNoMoreInteractions(ticketRepository);
        }

    }

    @Nested
    class UpdateTicketById {

        @Test
        void works() {
            // Arrange
            var ticketEntity = new TicketEntity();
            ticketEntity.setTicketId(123);

            when(ticketRepository.findById(ticketEntity.getTicketId())).thenReturn(Optional.of(ticketEntity));

            // Act
            ticketService.updateTicketById(ticketEntity);

            // Assert
            verify(ticketRepository).findById(ticketEntity.getTicketId());
            verify(travelMapper).updateTicketEntity(any(TicketEntity.class), any(TicketEntity.class));
            verify(ticketRepository).save(ticketEntity);

            verifyNoMoreInteractions(travelMapper, ticketRepository);
        }

        @Test
        void throwsNotFoundException_whenThereIsNoRecordPresent() {
            // Arrange
            var ticketEntity = new TicketEntity();
            ticketEntity.setTicketId(123);

            when(ticketRepository.findById(ticketEntity.getTicketId())).thenReturn(Optional.empty());

            // Act/Assert
            assertThatThrownBy(() -> ticketService.updateTicketById(ticketEntity))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void throwsBusinessValidationException_whenSavingTicketThrowsRunTimeException() {
            // Arrange
            var ticketEntity = new TicketEntity();
            ticketEntity.setTicketId(123);

            when(ticketRepository.findById(ticketEntity.getTicketId())).thenReturn(Optional.of(ticketEntity));

            when(ticketRepository.save(ticketEntity)).thenThrow(BusinessValidationException.class);

            // Act/Assert
            assertThatThrownBy(() -> ticketService.updateTicketById(ticketEntity))
                    .isInstanceOf(BusinessValidationException.class)
                    .hasMessage("The customerId/packageId is not a valid Id");
        }

    }

    @Nested
    class DeleteTicket {

        @Test
        void works() {
            // Arrange
            var ticketId = 123;

            var packageEntity = new PackageEntity();
            packageEntity.setCostPerPerson(5000);

            var ticketEntity = new TicketEntity();
            ticketEntity.setTicketId(ticketId);
            ticketEntity.setTotalMembers(4);
            ticketEntity.setPackageEntity(packageEntity);

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketEntity));

            // Act
            var refund = ticketService.deleteTicket(ticketId);

            // Assert
            assertThat(refund).isEqualTo(16000);

            verify(ticketRepository).findById(ticketId);
            verify(ticketRepository).deleteById(ticketId);

            verifyNoMoreInteractions(ticketRepository);
        }

        @Test
        void throwsNotFoundException_whenRecordIsNotPresent() {
            // Act/Assert
            assertThatThrownBy(() -> ticketService.deleteTicket(123))
                    .isInstanceOf(NotFoundException.class);
        }

    }

}
