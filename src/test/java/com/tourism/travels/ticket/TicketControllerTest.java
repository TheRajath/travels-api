package com.tourism.travels.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.querydsl.core.BooleanBuilder;
import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.packages.PackageService;
import com.tourism.travels.pojo.SearchRequest;
import com.tourism.travels.pojo.SearchTicketResource.TicketDetail;
import com.tourism.travels.pojo.TicketRefund;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private TravelMapper travelMapper;

    @Mock
    private TicketService ticketService;

    @Mock
    private PackageService packageService;

    @Mock
    private PredicateBuilder predicateBuilder;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private static final String TODAY_DATE = LocalDate.now().toString();

    @BeforeEach
    void setUp() {

        var ticketController = new TicketController(travelMapper, ticketService, packageService, predicateBuilder);

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
            ticketResource.setTicketId("123");
            ticketResource.setCustomerId("789");
            ticketResource.setPackageId("456");
            ticketResource.setTravelDate(LocalDate.parse("2022-10-12"));
            ticketResource.setTotalMembers("2");
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
            var ticketEntity = new TicketEntity();
            var ticketRequest = getTicketRequest();

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
        @CsvSource({"987,ticketId", "123,customerId", "999,packageId", "748,totalMembers"})
        void throws400BadException_whenTicketIdOrCustomerIdOrPackageIdOrTotalMembersOrTotalCostIsNull(String value,
                                                                                                      String fieldName)
                                                                                                      throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace(value, "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", fieldName).replace("null", "empty");

            // Act/Assert
            mockMvc.perform(put("/tickets/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTravelDateIsNotInProperFormat() throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace(TODAY_DATE, "2020-01-0123");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "travelDate")
                    .replace("must not be null", "date must be in correct format - yyyy-MM-dd");

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
            var request = TICKET_REQUEST.replace(TODAY_DATE, "2022-01-01");
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
            var predicate = new BooleanBuilder();
            var ticketEntity = new TicketEntity();
            var pageRequest = PageRequest.of(0, 25);

            PageImpl<TicketEntity> ticketEntities =
                    new PageImpl<>(Collections.singletonList(ticketEntity), pageRequest, 20);

            var ticketDetail = new TicketDetail();
            ticketDetail.setFirstName("firstName");
            ticketDetail.setLastName("lastName");
            ticketDetail.setEmail("email@email.com");
            ticketDetail.setPackageName("packageName");
            ticketDetail.setTripDuration("2 Days");
            ticketDetail.setTravelDate(LocalDate.parse("2022-12-15"));
            ticketDetail.setTotalMembers(2);
            ticketDetail.setTotalCostOfTrip(1500);

            when(predicateBuilder.buildSearchPredicate(any(SearchRequest.class))).thenReturn(predicate);
            when(ticketService.getTicketsBySearchPredicate(predicate, pageRequest)).thenReturn(ticketEntities);
            when(travelMapper.mapTicketDetails(ticketEntity)).thenReturn(ticketDetail);

            // Act/Assert
            mockMvc.perform(post("/tickets/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(SEARCH_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(SEARCH_TICKET_RESPONSE));

            verify(predicateBuilder).buildSearchPredicate(any(SearchRequest.class));
            verify(ticketService).getTicketsBySearchPredicate(predicate, pageRequest);
            verify(travelMapper).mapTicketDetails(ticketEntity);

            verifyNoMoreInteractions(predicateBuilder, ticketService, travelMapper);
        }

        @ParameterizedTest
        @CsvSource({"123, customerId", "987, packageId", "ias456@google.com, email"})
        void returns400BadRequest_whenCustomerIdOrPackageIdIsPresentAndIsEmpty(String value,
                                                                               String fieldName) throws Exception {
            // Arrange
            var requestBody = SEARCH_REQUEST.replace(value, "");

            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", fieldName)
                    .replace("must not be null", "must not be empty if present");

            // Act/Assert
            mockMvc.perform(post("/tickets/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenEmailIsInWrongFormat() throws Exception {
            // Arrange
            var requestBody = SEARCH_REQUEST.replace("ias456@google.com", "google");

            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "email")
                    .replace("must not be null", "must be a well-formed email address");

            // Act/Assert
            mockMvc.perform(post("/tickets/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTravelDateIsInWrongFormat() throws Exception {
            // Arrange
            var requestBody = SEARCH_REQUEST.replace("2022-12-15", "travelDate");

            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "travelDate")
                    .replace("must not be null", "travel date is in wrong format, correct format is yyyy-mm-dd");

            // Act/Assert
            mockMvc.perform(post("/tickets/search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }

    @Nested
    class UpdateTicket {

        @Test
        void works() throws Exception {
            // Arrange
            var ticketEntity = new TicketEntity();
            var ticketRequest = getTicketRequest();

            when(travelMapper.toTicketEntity(any(TicketRequest.class))).thenReturn(ticketEntity);
            when(ticketService.updateTicketById(ticketEntity)).thenReturn(ticketEntity);
            when(travelMapper.toTicketRequest(ticketEntity)).thenReturn(ticketRequest);

            // Act/Assert
            mockMvc.perform(put("/tickets/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TICKET_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(TICKET_REQUEST));

            verify(travelMapper).toTicketEntity(any(TicketRequest.class));
            verify(ticketService).updateTicketById(ticketEntity);
            verify(travelMapper).toTicketRequest(ticketEntity);

            verifyNoMoreInteractions(ticketService, travelMapper);
        }

        @ParameterizedTest
        @CsvSource({"987,ticketId", "123,customerId", "999,packageId", "748,totalMembers"})
        void throws400BadException_whenTicketIdOrCustomerIdOrPackageIdOrTotalMembersOrTotalCostIsNull(String value,
                                                                                                      String fieldName)
                throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace(value, "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", fieldName).replace("null", "empty");

            // Act/Assert
            mockMvc.perform(put("/tickets/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTravelDateIsNotInProperFormat() throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace(TODAY_DATE, "2020-01-0123");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "travelDate")
                    .replace("must not be null", "date must be in correct format - yyyy-MM-dd");

            // Act/Assert
            mockMvc.perform(put("/tickets/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTravelDateIsInPast() throws Exception {
            // Arrange
            var request = TICKET_REQUEST.replace(TODAY_DATE, "2022-01-01");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "travelDate")
                    .replace("must not be null", "must be a date in the present or in the future");

            // Act/Assert
            mockMvc.perform(put("/tickets/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }

    @Nested
    class CancelTicket {

        @Test
        void works() throws Exception {
            // Arrange
            var ticketRefund = new TicketRefund(200);
            var response = objectMapper.writeValueAsString(ticketRefund);

            when(ticketService.deleteTicket(1)).thenReturn(200);

            // Act/Assert
            mockMvc.perform(delete("/tickets/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(response));

            verify(ticketService).deleteTicket(1);

            verifyNoMoreInteractions(ticketService);
        }

    }

    private TicketRequest getTicketRequest() {

        var ticketRequest = new TicketRequest();
        ticketRequest.setTicketId("987");
        ticketRequest.setCustomerId("123");
        ticketRequest.setPackageId("999");
        ticketRequest.setTravelDate(TODAY_DATE);
        ticketRequest.setTotalMembers("748");

        return ticketRequest;
    }

    public static final String TICKET_DETAILS_RESPONSE =
            """
                    [
                      {
                        "ticketId": "123",
                        "customerId": "789",
                        "packageId": "456",
                        "travelDate": "2022-10-12",
                        "totalMembers": "2",
                        "totalCost": 3000
                      }
                    ]""";

    public static final String TICKET_REQUEST =
            """
                    {
                      "ticketId": "987",
                      "customerId": "123",
                      "packageId": "999",
                      "travelDate": "%s",
                      "totalMembers": "748"
                    }""".formatted(TODAY_DATE);

    public static final String SEARCH_REQUEST =
            """
                    {
                      "customerId": "123",
                      "packageId": "987",
                      "email": "ias456@google.com",
                      "travelDate": "2022-12-15"
                    }""";

    public static final String SEARCH_TICKET_RESPONSE =
            """
                    {
                      "pagination": {
                        "pageNumber": 0,
                        "pageSize": 25,
                        "totalReturnCount": 1,
                        "totalRowCount": 1
                      },
                      "ticketDetails": [
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
                      ]
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
