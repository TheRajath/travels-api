package com.tourism.travels.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.querydsl.core.types.Predicate;
import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.packages.PackageService;
import com.tourism.travels.pojo.SearchRequest;
import com.tourism.travels.pojo.SearchTicketResource;
import com.tourism.travels.pojo.TicketRequest;
import com.tourism.travels.pojo.TicketResource;
import com.tourism.travels.sql.PackageEntity;
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
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private PackageService packageService;

    @Mock
    private TravelMapper travelMapper;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        var ticketController = new TicketController(travelMapper, ticketService, packageService);

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Nested
    class GetTickets {

        @Test
        void works() throws Exception {
            // Arrange
            var packageId = 456;
            var ticketEntity = new TicketEntity();

            var packageEntity = new PackageEntity();
            packageEntity.setCostPerPerson(1500);

            var ticketResource = new TicketResource();
            ticketResource.setTicketId(123);
            ticketResource.setCustomerId(789);
            ticketResource.setPackageId(packageId);
            ticketResource.setTravelDate(LocalDate.parse("2022-10-12"));
            ticketResource.setTotalMembers(2);
            ticketResource.setTotalCost(3000);

            when(ticketService.getTicketEntities()).thenReturn(Collections.singletonList(ticketEntity));
            when(travelMapper.toTicketResource(ticketEntity)).thenReturn(ticketResource);
            when(packageService.getPackageEntityById(packageId)).thenReturn(packageEntity);

            // Act/Assert
            mockMvc.perform(get("/tickets"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(TICKET_DETAILS_RESPONSE));

            verify(ticketService).getTicketEntities();
            verify(travelMapper).toTicketResource(ticketEntity);
            verify(packageService).getPackageEntityById(packageId);

            verifyNoMoreInteractions(ticketService, travelMapper, packageService);
        }

    }

    @Nested
    class CreateTicket {

        @Test
        void works() throws Exception {
            // Arrange
            var ticketRequest = new TicketRequest();
            ticketRequest.setTicketId(10);
            ticketRequest.setCustomerId(70);
            ticketRequest.setPackageId(30);
            ticketRequest.setTravelDate(LocalDate.parse("2022-12-15"));
            ticketRequest.setTotalMembers(75);

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
        @CsvSource({"10,ticketId", "70,customerId", "30,packageId", "75,totalMembers"})
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
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "travelDate");

            // Act/Assert
            mockMvc.perform(put("/tickets/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTravelDateIsInPast() throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace("2022", "2021");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "travelDate")
                    .replace("must not be null", "must be a date in the present or in the future");

            // Act/Assert
            mockMvc.perform(put("/tickets/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }

    @Nested
    class SearchTicket {

        @Test
        void works() throws Exception {
            // Arrange
            var ticketEntity = new TicketEntity();
            var searchRequest = new SearchRequest();
            searchRequest.setTicketId(123);
            searchRequest.setCustomerId(364);
            searchRequest.setPackageId(765);
            searchRequest.setTravelDate(LocalDate.parse("2022-12-15"));

            var searchTicketResource = new SearchTicketResource();
            searchTicketResource.setFirstName("firstName");
            searchTicketResource.setLastName("lastName");
            searchTicketResource.setEmail("email@email.com");
            searchTicketResource.setPackageName("packageName");
            searchTicketResource.setTripDuration("2 Days");
            searchTicketResource.setTravelDate(LocalDate.parse("2022-12-15"));
            searchTicketResource.setTotalMembers(2);
            searchTicketResource.setTotalCostOfTrip(1500);

            var requestBody = objectMapper.writeValueAsString(searchRequest);

            when(ticketService.getTicketsBySearchPredicate(any(Predicate.class)))
                    .thenReturn(Collections.singletonList(ticketEntity));
            when(travelMapper.mapSearchResource(ticketEntity)).thenReturn(searchTicketResource);

            // Act/Assert
            mockMvc.perform(post("/tickets/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().json(SEARCH_TICKET_RESPONSE));

            verify(ticketService).getTicketsBySearchPredicate(any(Predicate.class));
            verify(travelMapper).mapSearchResource(ticketEntity);

            verifyNoMoreInteractions(ticketService, travelMapper);
        }

        @Test
        void returns400BadRequest_whenSearchRequestDoesNotContainCriteria() throws Exception {
            // Arrange
            var searchRequest = new SearchRequest();

            var requestBody = objectMapper.writeValueAsString(searchRequest);

            var message = "request body must contain at least one of the following search" +
                    " criteria: ticketId, customerId, packageId, travelDate";
            var errorMessage =
                    """
                            {
                                "message": "%s"
                            }""".formatted(message);

            // Act/Assert
            mockMvc.perform(post("/tickets/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
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
                        "customerId": 70,
                        "packageId": 30,
                        "travelDate": "2022-12-15",
                        "totalMembers": 75
                    }""";

    public static final String SEARCH_TICKET_RESPONSE =
            """
                    [
                        {
                            "firstName": "firstName",
                            "lastName": "lastName",
                            "email": "email@email.com",
                            "packageName": "packageName",
                            "tripDuration": "2 Days",
                            "travelDate": "2022-12-15",
                            "totalMembers": 2,
                            "totalCostOfTrip": 1500
                        }
                    ]""";

    private static final String COMMON_ERROR_MESSAGE =
            """
                    [
                        {
                            "field": "fieldName",
                            "message": "must not be null"
                        }
                    ]""";

}
