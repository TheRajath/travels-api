package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.GlobalExceptionHandler;
import com.tourism.travels.pojo.PackageRequest;
import com.tourism.travels.pojo.PackageResource;
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

        var packageController = new PackageController(travelMapper, packageService);

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
            var packageResource = getPackageResource();

            when(packageService.getPackageDetails()).thenReturn(Collections.singletonList(packageEntity));
            when(travelMapper.toPackageResource(packageEntity)).thenReturn(packageResource);

            // Act/Assert
            mockMvc.perform(get("/packages"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(PACKAGE_RESPONSE));

            verify(packageService).getPackageDetails();
            verify(travelMapper).toPackageResource(packageEntity);

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
            var packageResource = getPackageResource();

            when(packageService.getPackageEntityById(packageId)).thenReturn(packageEntity);
            when(travelMapper.toPackageResource(packageEntity)).thenReturn(packageResource);

            // Act/Assert
            mockMvc.perform(get("/packages/123"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(request));

            verify(packageService).getPackageEntityById(packageId);
            verify(travelMapper).toPackageResource(packageEntity);

            verifyNoMoreInteractions(packageService, travelMapper);
        }

    }

    @Nested
    class AddPackage {

        @Test
        void works() throws Exception {
            // Arrange
            var packageEntity = new PackageEntity();
            var packageRequest = getPackageRequest();

            when(travelMapper.toPackageEntity(any(PackageRequest.class))).thenReturn(packageEntity);
            when(packageService.addNewPackage(packageEntity)).thenReturn(packageEntity);
            when(travelMapper.toPackageRequest(packageEntity)).thenReturn(packageRequest);

            // Act/Assert
            mockMvc.perform(put("/packages/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(PACKAGE_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(PACKAGE_REQUEST));

            verify(travelMapper).toPackageEntity(any(PackageRequest.class));
            verify(packageService).addNewPackage(packageEntity);
            verify(travelMapper).toPackageRequest(packageEntity);

            verifyNoMoreInteractions(packageService, travelMapper);
        }

        @Test
        void returns400BadRequest_whenPackageIdIsNull() throws Exception {
            // Arrange
            var request = PACKAGE_REQUEST.replace("123", "null");
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
            var request = PACKAGE_REQUEST.replace("Agra", "");
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
            var request = PACKAGE_REQUEST.replace("4 Days", "");
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
            var request = PACKAGE_REQUEST.replace("5000", "null");
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
    class UpdatePackage {

        @Test
        void works() throws Exception {
            // Arrange
            var packageEntity = new PackageEntity();
            var packageRequest = getPackageRequest();

            when(travelMapper.toPackageEntity(any(PackageRequest.class))).thenReturn(packageEntity);
            when(packageService.updateExistingPackage(packageEntity)).thenReturn(packageEntity);
            when(travelMapper.toPackageRequest(packageEntity)).thenReturn(packageRequest);

            // Act/Assert
            mockMvc.perform(put("/packages/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(PACKAGE_REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(content().json(PACKAGE_REQUEST));

            verify(travelMapper).toPackageEntity(any(PackageRequest.class));
            verify(packageService).updateExistingPackage(packageEntity);
            verify(travelMapper).toPackageRequest(packageEntity);

            verifyNoMoreInteractions(packageService, travelMapper);
        }

        @Test
        void returns400BadRequest_whenPackageIdIsNull() throws Exception {
            // Arrange
            var request = PACKAGE_REQUEST.replace("123", "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "packageId").replace("empty", "null");

            // Act/Assert
            mockMvc.perform(put("/packages/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenPackageNameIsEmpty() throws Exception {
            // Arrange
            var request = PACKAGE_REQUEST.replace("Agra", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "packageName");

            // Act/Assert
            mockMvc.perform(put("/packages/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenTripDurationIsEmpty() throws Exception {
            // Arrange
            var request = PACKAGE_REQUEST.replace("4 Days", "");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "tripDuration");

            // Act/Assert
            mockMvc.perform(put("/packages/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(errorMessage));
        }

        @Test
        void returns400BadRequest_whenCostPerPersonIsNull() throws Exception {
            // Arrange
            var request = PACKAGE_REQUEST.replace("5000", "null");
            var errorMessage = COMMON_ERROR_MESSAGE.replace("fieldName", "costPerPerson").replace("empty", "null");

            // Act/Assert
            mockMvc.perform(put("/packages/update")
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

    private PackageRequest getPackageRequest() {

        var packageRequest = new PackageRequest();
        packageRequest.setPackageId(123);
        packageRequest.setPackageName("Agra");
        packageRequest.setTripDuration("4 Days");
        packageRequest.setCostPerPerson(5000);

        return packageRequest;
    }

    private PackageResource getPackageResource() {

        var packageResource = new PackageResource();

        packageResource.setPackageId(123);
        packageResource.setPackageName("Agra");
        packageResource.setTripDuration("2 Days,1 Night");
        packageResource.setTotalCost(5000);

        return packageResource;
    }

    private static final String PACKAGE_RESPONSE =
            """
                    [
                        {
                            "packageId": 123,
                            "packageName": "Agra",
                            "tripDuration": "2 Days,1 Night",
                            "totalCost": 5000
                        }
                    ]""";

    private static final String PACKAGE_REQUEST =
            """
                    {
                        "packageId": 123,
                        "packageName": "Agra",
                        "tripDuration": "4 Days",
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
