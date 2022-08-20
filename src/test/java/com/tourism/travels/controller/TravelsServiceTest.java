package com.tourism.travels.controller;

import com.tourism.travels.exception.AlreadyExistsException;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.CustomerRepository;
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
class TravelsServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private CustomerRepository customerRepository;

    private TravelsService travelsService;

    @BeforeEach
    void setup() {

        travelsService = new TravelsService(packageRepository, customerRepository);
    }

    @Nested
    class GetPackageDetails {

        @Test
        void works() {
            // Arrange
            var packageEntities = Collections.singletonList(new PackageEntity());

            when(packageRepository.findAll()).thenReturn(packageEntities);

            // Act
            var packageDetails = travelsService.getPackageDetails();

            //Assert
            assertThat(packageDetails).isEqualTo(packageEntities);

            verify(packageRepository).findAll();

            verifyNoMoreInteractions(packageRepository);
        }

    }

    @Nested
    class SignUp {

        @Test
        void works() {
            // Arrange
            var customerEntity = new CustomerEntity();
            customerEntity.setEmail("email@gmail.com");

            // Act
            travelsService.signUp(customerEntity);

            // Assert
            verify(customerRepository).findByEmail(customerEntity.getEmail());
            verify(customerRepository).save(customerEntity);

            verifyNoMoreInteractions(customerRepository);
        }

        @Test
        void throwsAlreadyExistsException_whenThereIsAnExistingRecord() {
            // Arrange
            var customerEntity = new CustomerEntity();
            customerEntity.setEmail("email@gmail.com");

            when(customerRepository.findByEmail(customerEntity.getEmail())).thenReturn(Optional.of(customerEntity));

            // Act/Assert
            assertThatThrownBy(() -> travelsService.signUp(customerEntity))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage("Customer with this email: email@gmail.com already exists");
        }

    }

}
