package com.tourism.travels.customer;

import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.pojo.CustomerRequest;
import com.tourism.travels.pojo.CustomerResource;
import com.tourism.travels.sql.CustomerEntity;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private TravelMapper travelMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        var customerController = new CustomerController(travelMapper, customerService);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    class GetCustomers {

        @Test
        void works() throws Exception {
            // Arrange
            var customerEntity = new CustomerEntity();
            var customerResource = getCustomerResource();

            when(customerService.getCustomerDetails()).thenReturn(Collections.singletonList(customerEntity));
            when(travelMapper.toCustomerResource(customerEntity)).thenReturn(customerResource);

            // Act/Assert
            mockMvc.perform(get("/customers"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(CUSTOMER_DETAILS_RESPONSE));

            verify(customerService).getCustomerDetails();
            verify(travelMapper).toCustomerResource(customerEntity);

            verifyNoMoreInteractions(customerService, travelMapper);
        }

    }

    @Nested
    class GetCustomerById {

        @Test
        void works() throws Exception {
            // Arrange
            var customerId = 123;
            var customerEntity = new CustomerEntity();
            var request = CUSTOMER_DETAILS_RESPONSE.replace("[", "");
            var customerResource = getCustomerResource();

            when(customerService.getCustomerEntityById(customerId)).thenReturn(customerEntity);
            when(travelMapper.toCustomerResource(customerEntity)).thenReturn(customerResource);

            // Act/Assert
            mockMvc.perform(get("/customers/123"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(request));

            verify(customerService).getCustomerEntityById(customerId);
            verify(travelMapper).toCustomerResource(customerEntity);

            verifyNoMoreInteractions(customerService, travelMapper);
        }

    }

    @Nested
    class SignUpCustomer {

        @Test
        void works() throws Exception {
            // Arrange
            var customerRequest = new CustomerRequest();
            customerRequest.setCustomerId(123);
            customerRequest.setFirstName("firstName");
            customerRequest.setLastName("lastName");
            customerRequest.setEmail("email@gmail.com");
            customerRequest.setPassword("secret");

            var customerEntity = new CustomerEntity();

            when(travelMapper.toCustomerEntity(any(CustomerRequest.class))).thenReturn(customerEntity);
            when(customerService.signUp(customerEntity)).thenReturn(customerEntity);
            when(travelMapper.toCustomerRequest(customerEntity)).thenReturn(customerRequest);

            // Act/Assert
            mockMvc.perform(put("/customers/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(CUSTOMER_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(CUSTOMER_REQUEST));

            verify(travelMapper).toCustomerEntity(any(CustomerRequest.class));
            verify(customerService).signUp(customerEntity);
            verify(travelMapper).toCustomerRequest(customerEntity);

            verifyNoMoreInteractions(customerService, travelMapper);
        }

        @Test
        void return400BadException_whenCustomerIdIsNull() throws Exception {
            // Arrange
            var request = CUSTOMER_REQUEST.replace("123", "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "customerId").replace("empty", "null");

            // Act/Assert
            mockMvc.perform(put("/customers/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @ParameterizedTest
        @CsvSource({"firstName,firstName", "email@gmail.com,email", "secret,password"})
        void return400BadException_whenFirstNameOrEmailOrPasswordIsEmpty(String value, String fieldName) throws Exception {
            // Arrange
            var request = CUSTOMER_REQUEST.replace(value, "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", fieldName);

            // Act/Assert
            mockMvc.perform(put("/customers/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void return400BadException_whenEmailIsNotInProperForm() throws Exception {
            // Arrange
            var request = CUSTOMER_REQUEST.replace("email@gmail.com", "email");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "email")
                    .replace("must not be empty", "must be a well-formed email address");

            // Act/Assert
            mockMvc.perform(put("/customers/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }

    @Nested
    class UpdateCustomerDetails {

        @Test
        void works() throws Exception {
            // Arrange
            var customerRequest = new CustomerRequest();
            customerRequest.setCustomerId(123);
            customerRequest.setFirstName("firstName");
            customerRequest.setLastName("lastName");
            customerRequest.setEmail("email@gmail.com");
            customerRequest.setPassword("secret");

            var customerEntity = new CustomerEntity();

            when(travelMapper.toCustomerEntity(any(CustomerRequest.class))).thenReturn(customerEntity);
            when(customerService.updateCustomer(customerEntity)).thenReturn(customerEntity);
            when(travelMapper.toCustomerRequest(customerEntity)).thenReturn(customerRequest);

            // Act/Assert
            mockMvc.perform(put("/customers/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(CUSTOMER_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(CUSTOMER_REQUEST));

            verify(travelMapper).toCustomerEntity(any(CustomerRequest.class));
            verify(customerService).updateCustomer(customerEntity);
            verify(travelMapper).toCustomerRequest(customerEntity);

            verifyNoMoreInteractions(customerService, travelMapper);
        }

        @Test
        void return400BadException_whenCustomerIdIsNull() throws Exception {
            // Arrange
            var request = CUSTOMER_REQUEST.replace("123", "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "customerId").replace("empty", "null");

            // Act/Assert
            mockMvc.perform(put("/customers/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @ParameterizedTest
        @CsvSource({"firstName,firstName", "email@gmail.com,email", "secret,password"})
        void return400BadException_whenFirstNameOrEmailOrPasswordIsEmpty(String value, String fieldName) throws Exception {
            // Arrange
            var request = CUSTOMER_REQUEST.replace(value, "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", fieldName);

            // Act/Assert
            mockMvc.perform(put("/customers/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void return400BadException_whenEmailIsNotInProperForm() throws Exception {
            // Arrange
            var request = CUSTOMER_REQUEST.replace("email@gmail.com", "email");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "email")
                    .replace("must not be empty", "must be a well-formed email address");

            // Act/Assert
            mockMvc.perform(put("/customers/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }
    @Nested
    class DeleteCustomer {

        @Test
        void works() throws Exception {
            // Act/Assert
            mockMvc.perform(delete("/customers/1"))
                    .andExpect(status().isNoContent());

            verify(customerService).deleteByCustomerId(1);

            verifyNoMoreInteractions(customerService);
        }

    }

    private CustomerResource getCustomerResource() {

        var customerResource = new CustomerResource();
        customerResource.setCustomerId(123);
        customerResource.setFirstName("firstName");
        customerResource.setLastName("lastName");
        customerResource.setEmail("email@gmail.com");
        customerResource.setPassword("savedPassword");

        return customerResource;
    }

    private static final String CUSTOMER_DETAILS_RESPONSE =
            """
                    [
                      {
                        "customerId": 123,
                        "firstName": "firstName",
                        "lastName": "lastName",
                        "email": "email@gmail.com",
                        "password": "savedPassword"
                      }
                    ]""";

    private static final String CUSTOMER_REQUEST =
            """
                    {
                      "customerId": 123,
                      "firstName": "firstName",
                      "lastName": "lastName",
                      "email": "email@gmail.com",
                      "password": "secret"
                    }""";

    private static final String COMMON_ERROR_MESSAGE =
            """
                    [
                      {
                        "field": "fieldName",
                        "message": "must not be empty"
                      }
                    ]""";

}
