package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.pojo.AddPackageRequest;
import com.tourism.travels.pojo.PackageDetailsResource;
import com.tourism.travels.sql.PackageEntity;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
            var packageEntity = new PackageEntity();
            var packageDetailsResource = getPackageDetailsResource();

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

    @Nested
    class GetPackageById {

        @Test
        void works() throws Exception {
            // Arrange
            var packageId = 123;
            var packageEntity = new PackageEntity();
            var request = PACKAGE_RESPONSE.replace("[", "");
            var packageDetailsResource = getPackageDetailsResource();

            when(packageService.getPackageEntityById(packageId)).thenReturn(packageEntity);
            when(travelMapper.toPackageDetailsResource(packageEntity)).thenReturn(packageDetailsResource);

            // Act/Assert
            mockMvc.perform(get("/packages/123"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(request));

            verify(packageService).getPackageEntityById(packageId);
            verify(travelMapper).toPackageDetailsResource(packageEntity);

            verifyNoMoreInteractions(packageService, travelMapper);
        }

    }

    @Nested
    class AddPackage {

        @Test
        void works() throws Exception {
            // Arrange
            var addPackageRequest = new AddPackageRequest();
            addPackageRequest.setPackageId(123);
            addPackageRequest.setPackageName("Agra");
            addPackageRequest.setTripDuration("4 Day");
            addPackageRequest.setCostPerPerson(5000);

            var packageEntity = new PackageEntity();

            when(travelMapper.toPackageEntity(any(AddPackageRequest.class))).thenReturn(packageEntity);
            when(packageService.addNewPackage(packageEntity)).thenReturn(packageEntity);
            when(travelMapper.toAddPackageRequest(packageEntity)).thenReturn(addPackageRequest);

            // Act/Assert
            mockMvc.perform(put("/packages/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ADD_PACKAGE_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(ADD_PACKAGE_REQUEST));

            verify(travelMapper).toPackageEntity(any(AddPackageRequest.class));
            verify(packageService).addNewPackage(packageEntity);
            verify(travelMapper).toAddPackageRequest(packageEntity);

            verifyNoMoreInteractions(packageService, travelMapper);
        }

        @Test
        void returns400BadRequest_whenPackageIdIsNull() throws Exception {
            // Arrange
            var request = ADD_PACKAGE_REQUEST.replace("123", "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "packageId").replace("empty", "null");

            // Act/Assert
            mockMvc.perform(put("/packages/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenPackageNameIsEmpty() throws Exception {
            // Arrange
            var request = ADD_PACKAGE_REQUEST.replace("Agra", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "packageName");

            // Act/Assert
            mockMvc.perform(put("/packages/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTripDurationIsEmpty() throws Exception {
            // Arrange
            var request = ADD_PACKAGE_REQUEST.replace("4 Day", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "tripDuration");

            // Act/Assert
            mockMvc.perform(put("/packages/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenCostPerPersonIsNull() throws Exception {
            // Arrange
            var request = ADD_PACKAGE_REQUEST.replace("5000", "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "costPerPerson").replace("empty", "null");

            // Act/Assert
            mockMvc.perform(put("/packages/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

    }

    @Nested
    class DeletePackage {

        @Test
        void works() throws Exception {
            // Act/Assert
            mockMvc.perform(delete("/packages/1"))
                    .andExpect(status().isNoContent());

            verify(packageService).deleteByPackageId(1);

            verifyNoMoreInteractions(packageService);
        }

    }

    private PackageDetailsResource getPackageDetailsResource() {

        var packageDetailsResource = new PackageDetailsResource();

        packageDetailsResource.setId(123);
        packageDetailsResource.setPackageName("Agra");
        packageDetailsResource.setTripDuration("2 Days,1 Night");
        packageDetailsResource.setTotalCost(5000);

        return packageDetailsResource;
    }

    private static final String PACKAGE_RESPONSE =
            """
                    [
                        {
                            "id": 123,
                            "packageName": "Agra",
                            "tripDuration": "2 Days,1 Night",
                            "totalCost": 5000
                        }
                    ]""";

    private static final String ADD_PACKAGE_REQUEST =
            """
                    {
                        "packageId": 123,
                        "packageName": "Agra",
                        "tripDuration": "4 Day",
                        "costPerPerson": 5000
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
