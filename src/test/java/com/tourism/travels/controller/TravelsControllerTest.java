package com.tourism.travels.controller;

import com.tourism.travels.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class TravelsControllerTest {

    @Mock
    private TravelsService travelsService;

    @Mock
    private TravelMapper travelMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        var travelsController = new TravelsController(travelsService, travelMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(travelsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    class GetPackages {

        @Test
        void works() throws Exception {
            // Arrange
            var packageDetailsResource = new PackageDetailsResource();
            packageDetailsResource.setPackageName("Agra");
            packageDetailsResource.setTripDuration("2 Days,1 Night");
            packageDetailsResource.setTotalCost(5000);

            var packageEntity = new PackageEntity();

            when(travelsService.getPackageDetails()).thenReturn(Collections.singletonList(packageEntity));
            when(travelMapper.toPackageDetailsResource(packageEntity)).thenReturn(packageDetailsResource);

            // Act/Assert
            mockMvc.perform(get("/packages"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(PACKAGE_RESPONSE));

            verify(travelsService).getPackageDetails();
            verify(travelMapper).toPackageDetailsResource(packageEntity);

            verifyNoMoreInteractions(travelsService, travelMapper);

        }

    }

    @Nested
    class SignUpCustomer {

        @Test
        void works() throws Exception {
            // Arrange
            var customerSignUp = new CustomerSignUp();
            customerSignUp.setCustomerId(123);
            customerSignUp.setFirstName("firstName");
            customerSignUp.setLastName("lastName");
            customerSignUp.setEmail("email@gmail.com");
            customerSignUp.setPassword("secret");

            var customerEntity = new CustomerEntity();

            when(travelMapper.toCustomerEntity(any(CustomerSignUp.class))).thenReturn(customerEntity);
            when(travelsService.signUp(customerEntity)).thenReturn(customerEntity);
            when(travelMapper.toSignUpRequest(customerEntity)).thenReturn(customerSignUp);

            // Act/Assert
            mockMvc.perform(put("/customer/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(CUSTOMER_SIGN_UP))
                    .andExpect(status().isOk())
                    .andExpect(content().json(CUSTOMER_SIGN_UP));

            verify(travelMapper).toCustomerEntity(any(CustomerSignUp.class));
            verify(travelsService).signUp(customerEntity);
            verify(travelMapper).toSignUpRequest(customerEntity);

            verifyNoMoreInteractions(travelsService, travelMapper);
        }

        @Test
        void return400BadException_whenCustomerIdIsNull() throws Exception {
            // Arrange
            var request = CUSTOMER_SIGN_UP.replace("123", "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "customerId").replace("empty", "null");

            // Act/Assert
            mockMvc.perform(put("/customer/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void return400BadException_whenFirstNameIsEmpty() throws Exception {
            // Arrange
            var request = CUSTOMER_SIGN_UP.replace("firstName", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "firstName");

            // Act/Assert
            mockMvc.perform(put("/customer/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void return400BadException_whenEmailIsNotInProperForm() throws Exception {
            // Arrange
            var request = CUSTOMER_SIGN_UP.replace("email@gmail.com", "email");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "email")
                    .replace("must not be empty", "must be a well-formed email address");

            // Act/Assert
            mockMvc.perform(put("/customer/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void return400BadException_whenEmailIsEmpty() throws Exception {
            // Arrange
            var request = CUSTOMER_SIGN_UP.replace("email@gmail.com", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "email");

            // Act/Assert
            mockMvc.perform(put("/customer/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void return400BadException_whenPasswordIsEmpty() throws Exception {
            // Arrange
            var request = CUSTOMER_SIGN_UP.replace("secret", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "password");

            // Act/Assert
            mockMvc.perform(put("/customer/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }

    private static final String PACKAGE_RESPONSE =
            """
                    [
                        {
                            "packageName": "Agra",
                            "tripDuration": "2 Days,1 Night",
                            "totalCost": 5000
                        }
                    ]""";

    private static final String CUSTOMER_SIGN_UP =
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
