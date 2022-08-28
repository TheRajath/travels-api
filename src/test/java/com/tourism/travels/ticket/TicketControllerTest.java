package com.tourism.travels.ticket;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.pojo.TicketResource;
import com.tourism.travels.sql.TicketEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

}
