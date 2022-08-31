package com.tourism.travels.customer;

import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.exception.NotFoundException;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.CustomerRepository;
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
class CustomerServiceTest {

    @Mock
    private TravelMapper travelMapper;

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setup() {

        customerService = new CustomerService(travelMapper, customerRepository);
    }

    @Nested
    class GetCustomerDetails {

        @Test
        void works() {
            // Arrange
            var customerEntities = Collections.singletonList(new CustomerEntity());

            when(customerRepository.findAll()).thenReturn(customerEntities);

            // Act
            var customerDetails = customerService.getCustomerDetails();

            // Assert
            assertThat(customerDetails).isEqualTo(customerEntities);

            verify(customerRepository).findAll();

            verifyNoMoreInteractions(customerRepository);
        }

    }

    @Nested
    class GetCustomerEntityById {

        @Test
        void works() {
            // Arrange
            var customerId = 123;
            var customerEntity = new CustomerEntity();

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

            // Act
            var returnedCustomerEntity = customerService.getCustomerEntityById(customerId);

            // Assert
            assertThat(returnedCustomerEntity).isEqualTo(customerEntity);

            verify(customerRepository).findById(customerId);

            verifyNoMoreInteractions(customerRepository);
        }

        @Test
        void throwsNotFoundException_whenEntityIsNotFound() {
            // Arrange
            var customerId = 123;

            when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

            // Act/ Assert
            assertThatThrownBy(() -> customerService.getCustomerEntityById(customerId))
                    .isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class SignUp {

        @Test
        void works() {
            // Arrange
            var customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(123);

            // Act
            customerService.signUp(customerEntity);

            // Assert
            verify(customerRepository).findById(customerEntity.getCustomerId());
            verify(customerRepository).save(customerEntity);

            verifyNoMoreInteractions(customerRepository);
        }

        @Test
        void throwsAlreadyExistsException_whenThereIsAnExistingRecord() {
            // Arrange
            var customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(123);

            when(customerRepository.findById(customerEntity.getCustomerId())).thenReturn(Optional.of(customerEntity));

            // Act/Assert
            assertThatThrownBy(() -> customerService.signUp(customerEntity))
                    .isInstanceOf(BusinessValidationException.class)
                    .hasMessage("Customer with this customerId: 123 already exists");
        }

    }

    @Nested
    class UpdateCustomer {

        @Test
        void works() {
            // Arrange
            var customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(123);

            when(customerRepository.findById(customerEntity.getCustomerId())).thenReturn(Optional.of(customerEntity));

            // Act
            customerService.updateCustomer(customerEntity);

            // Assert
            verify(customerRepository).findById(customerEntity.getCustomerId());
            verify(travelMapper).updateCustomerEntity(any(CustomerEntity.class), any(CustomerEntity.class));
            verify(customerRepository).save(customerEntity);

            verifyNoMoreInteractions(travelMapper, customerRepository);
        }

        @Test
        void throwsNotFoundException_whenThereIsNoRecordPresent() {
            // Arrange
            var customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(123);

            when(customerRepository.findById(customerEntity.getCustomerId())).thenReturn(Optional.empty());

            // Act/Assert
            assertThatThrownBy(() -> customerService.updateCustomer(customerEntity))
                    .isInstanceOf(NotFoundException.class);
        }

    }

    @Nested
    class DeleteByCustomerId {

        @Test
        void works() {
            // Arrange
            var customerId = 123;

            var customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(customerId);

            when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

            // Act
            customerService.deleteByCustomerId(customerId);
            
            // Assert
            verify(customerRepository).deleteById(customerId);

            verifyNoMoreInteractions(customerRepository);
        }

        @Test
        void throwsNotFoundException_whenRecordIsNotPresent() {
            // Act/Assert
            assertThatThrownBy(() -> customerService.deleteByCustomerId(123))
                    .isInstanceOf(NotFoundException.class);
        }

    }

}
