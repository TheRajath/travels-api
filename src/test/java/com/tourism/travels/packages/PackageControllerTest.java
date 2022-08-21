package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.pojo.PackageDetailsResource;
import com.tourism.travels.sql.PackageEntity;
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
class PackageControllerTest {

    @Mock
    private PackageService packageService;

    @Mock
    private TravelMapper travelMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        var packageController = new PackageController(packageService, travelMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(packageController)
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

            when(packageService.getPackageDetails()).thenReturn(Collections.singletonList(packageEntity));
            when(travelMapper.toPackageDetailsResource(packageEntity)).thenReturn(packageDetailsResource);

            // Act/Assert
            mockMvc.perform(get("/packages"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(PACKAGE_RESPONSE));

            verify(packageService).getPackageDetails();
            verify(travelMapper).toPackageDetailsResource(packageEntity);

            verifyNoMoreInteractions(packageService, travelMapper);

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

}
