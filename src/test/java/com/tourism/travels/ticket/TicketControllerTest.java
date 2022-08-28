package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.pojo.TicketRequest;
import com.tourism.travels.pojo.TicketResource;
import com.tourism.travels.sql.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private TravelMapper travelMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        var ticketController = new TicketController(travelMapper, ticketService);

        mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    class GetTickets {

        @Test
        void works() throws Exception {
            // Arrange
            var ticketEntity = new TicketEntity();
            var ticketResource = new TicketResource();
            ticketResource.setTicketId(123);
            ticketResource.setCustomerId(789);
            ticketResource.setPackageId(456);
            ticketResource.setTravelDate("2022-10-12");
            ticketResource.setTotalMembers(2);
            ticketResource.setTotalCost(3000);

            when(ticketService.getTicketEntities()).thenReturn(Collections.singletonList(ticketEntity));
            when(travelMapper.toTicketResource(ticketEntity)).thenReturn(ticketResource);

            // Act/Assert
            mockMvc.perform(get("/tickets"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(TICKET_DETAILS_RESPONSE));

            verify(ticketService).getTicketEntities();
            verify(travelMapper).toTicketResource(ticketEntity);

            verifyNoMoreInteractions(ticketService, travelMapper);
        }

    }

    @Nested
    class CreateTicket {

        @Test
        void works() throws Exception {
            // Arrange
            var ticketRequest = new TicketRequest();
            ticketRequest.setTicketId(10);
            ticketRequest.setCustomerId(20);
            ticketRequest.setPackageId(30);
            ticketRequest.setTravelDate("2022-12-15");
            ticketRequest.setTotalMembers(15);
            ticketRequest.setTotalCost(90);

            var ticketEntity = new TicketEntity();

            when(travelMapper.toTicketEntity(any(TicketRequest.class))).thenReturn(ticketEntity);
            when(ticketService.createTicket(ticketEntity)).thenReturn(ticketEntity);
            when(travelMapper.toTicketRequest(ticketEntity)).thenReturn(ticketRequest);

            // Act/Assert
            mockMvc.perform(put("/tickets/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TICKET_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(TICKET_REQUEST));

            verify(travelMapper).toTicketEntity(any(TicketRequest.class));
            verify(ticketService).createTicket(ticketEntity);
            verify(travelMapper).toTicketRequest(ticketEntity);

            verifyNoMoreInteractions(ticketService, travelMapper);
        }

        @ParameterizedTest
        @CsvSource({"10,ticketId", "20,customerId", "30,packageId", "15,totalMembers"})
        void throws400BadException_whenTicketIdOrCustomerIdOrPackageIdOrTotalMembersOrTotalCostIsNull(String value,
                                                                                                      String fieldName)
                                                                                                      throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace(value, "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", fieldName);

            // Act/Assert
            mockMvc.perform(put("/tickets/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTravelDateIsNotPresent() throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace("2022-12-15", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "travelDate").replace("null", "empty");

            // Act/Assert
            mockMvc.perform(put("/tickets/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }

    public static final String TICKET_DETAILS_RESPONSE =
            """
                    [
                        {
                            "ticketId": 123,
                            "customerId": 789,
                            "packageId": 456,
                            "travelDate": "2022-10-12",
                            "totalMembers": 2,
                            "totalCost": 3000
                        }
                    ]""";

    public static final String TICKET_REQUEST =
            """
                    {
                        "ticketId": 10,
                        "customerId": 20,
                        "packageId": 30,
                        "travelDate": "2022-12-15",
                        "totalMembers": 15,
                        "totalCost": 90
                    }""";

    private static final String COMMON_ERROR_MESSAGE =
            """
                    [
                        {
                            "field": "fieldName",
                            "message": "must not be null"
                        }
                    ]""";

}
