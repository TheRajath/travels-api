package com.tourism.travels.packages;

import com.tourism.travels.customer.TravelMapper;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.exception.NotFoundException;
import com.tourism.travels.sql.PackageEntity;
import com.tourism.travels.sql.PackageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock
    private TravelMapper travelMapper;

    @Mock
    private PackageRepository packageRepository;

    private PackageService packageService;

    @BeforeEach
    void setup() {

        packageService = new PackageService(travelMapper, packageRepository);
    }

    @Nested
    class GetPackageDetails {

        @Test
        void works() {
            // Arrange
            var packageEntities = Collections.singletonList(new PackageEntity());

            when(packageRepository.findAll()).thenReturn(packageEntities);

            // Act
            var packageDetails = packageService.getPackageDetails();

            //Assert
            assertThat(packageDetails).isEqualTo(packageEntities);

            verify(packageRepository).findAll();

            verifyNoMoreInteractions(packageRepository);
        }

    }

    @Nested
    class GetPackageEntityById {

        @Test
        void works() {
            // Arrange
            var packageId = 123;
            var packageEntity = new PackageEntity();

            when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));

            // Act
            var returnedPackageEntity = packageService.getPackageEntityById(packageId);

            // Assert
            assertThat(returnedPackageEntity).isEqualTo(packageEntity);

            verify(packageRepository).findById(packageId);

            verifyNoMoreInteractions(packageRepository);
        }

        @Test
        void throwsNotFoundException_whenEntityIsNotFound() {
            // Arrange
            var packageId = 123;

            when(packageRepository.findById(packageId)).thenReturn(Optional.empty());

            // Act/ Assert
            assertThatThrownBy(() -> packageService.getPackageEntityById(packageId))
                    .isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class AddNewPackage {

        @Test
        void works() {
            // Arrange
            var packageEntity = new PackageEntity();
            packageEntity.setPackageId(123);

            // Act
            packageService.addNewPackage(packageEntity);

            // Assert
            verify(packageRepository).findById(packageEntity.getPackageId());
            verify(packageRepository).save(packageEntity);

            verifyNoMoreInteractions(packageRepository);
        }

        @Test
        void throwsAlreadyExistsException_whenThereIsAnExistingRecord() {
            // Arrange
            var packageEntity = new PackageEntity();
            packageEntity.setPackageId(123);

            when(packageRepository.findById(packageEntity.getPackageId())).thenReturn(Optional.of(packageEntity));

            // Act/Assert
            assertThatThrownBy(() -> packageService.addNewPackage(packageEntity))
                    .isInstanceOf(BusinessValidationException.class)
                    .hasMessage("Package with is id: 123 already exists");
        }

    }

    @Nested
    class UpdateExistingPackage {

        @Test
        void works() {
            // Arrange
            var packageEntity = new PackageEntity();
            packageEntity.setPackageId(123);

            when(packageRepository.findById(packageEntity.getPackageId())).thenReturn(Optional.of(packageEntity));

            // Act
            packageService.updateExistingPackage(packageEntity);

            // Assert
            verify(packageRepository).findById(packageEntity.getPackageId());
            verify(travelMapper).updatePackageEntity(any(PackageEntity.class), any(PackageEntity.class));
            verify(packageRepository).save(packageEntity);

            verifyNoMoreInteractions(travelMapper, packageRepository);
        }

        @Test
        void throwsNotFoundException_whenThereIsNoRecordPresent() {
            // Arrange
            var packageEntity = new PackageEntity();
            packageEntity.setPackageId(123);

            when(packageRepository.findById(packageEntity.getPackageId())).thenReturn(Optional.empty());

            // Act/Assert
            assertThatThrownBy(() -> packageService.updateExistingPackage(packageEntity))
                    .isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class DeleteByPackageId {

        @Test
        void works() {
            // Arrange
            var packageId = 123;

            var packageEntity = new PackageEntity();
            packageEntity.setPackageId(packageId);

            when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));

            // Act
            packageService.deleteByPackageId(packageId);

            // Assert
            verify(packageRepository).findById(packageId);
            verify(packageRepository).deleteById(packageId);

            verifyNoMoreInteractions(packageRepository);
        }

        @Test
        void throwsNotFoundException_whenRecordIsNotPresent() {
            // Act/Assert
            assertThatThrownBy(() -> packageService.deleteByPackageId(123))
                    .isInstanceOf(NotFoundException.class);
        }

    }

}
