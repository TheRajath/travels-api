package com.tourism.travels.packages;

import com.tourism.travels.exception.AlreadyExistsException;
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
    private PackageRepository packageRepository;

    private PackageService packageService;

    @BeforeEach
    void setup() {

        packageService = new PackageService(packageRepository);
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
            packageEntity.setId(123);

            // Act
            packageService.addNewPackage(packageEntity);

            // Assert
            verify(packageRepository).findById(packageEntity.getId());
            verify(packageRepository).save(packageEntity);

            verifyNoMoreInteractions(packageRepository);
        }

        @Test
        void throwsAlreadyExistsException_whenThereIsAnExistingRecord() {
            // Arrange
            var packageEntity = new PackageEntity();
            packageEntity.setId(123);

            when(packageRepository.findById(packageEntity.getId())).thenReturn(Optional.of(packageEntity));

            // Act/Assert
            assertThatThrownBy(() -> packageService.addNewPackage(packageEntity))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage("Package with is id: 123 already exists");
        }

    }

    @Nested
    class DeleteByPackageId {

        @Test
        void works() {
            // Arrange
            var packageId = 123;

            var packageEntity = new PackageEntity();
            packageEntity.setId(packageId);

            when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));

            // Act
            packageService.deleteByPackageId(packageId);

            // Assert
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
