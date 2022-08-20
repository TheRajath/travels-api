package com.tourism.travels.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TravelsControllerTest {

    @Mock
    private TravelsService travelsService;

    @Mock
    private TravelMapper travelMapper;

    private TravelsController travelsController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        travelsController = new TravelsController(travelsService, travelMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(travelsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getPackagesTest() throws Exception {
        // Arrange
        var packageDetailsResource = new PackageDetailsResource();
        packageDetailsResource.setPackageName("Bangalore");
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

    @Test
    void getTotalPackagesTest() {
        // Arrange/Act
        var returnedNumberOfPackages = travelsController.getTotalPackages();

        // Assert
        assertThat(returnedNumberOfPackages).isEqualTo(20);

    }

    private static final String PACKAGE_RESPONSE =
            """
                    [
                        {
                            "packageName": "Bangalore",
                            "tripDuration": "2 Days,1 Night",
                            "totalCost": 5000
                        }
                    ]""";
}
