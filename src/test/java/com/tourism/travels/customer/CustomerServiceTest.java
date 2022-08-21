package com.tourism.travels.customer;

import com.tourism.travels.exception.AlreadyExistsException;
import com.tourism.travels.sql.CustomerEntity;
import com.tourism.travels.sql.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setup() {

        customerService = new CustomerService(customerRepository);
    }

    @Nested
    class SignUp {

        @Test
        void works() {
            // Arrange
            var customerEntity = new CustomerEntity();
            customerEntity.setEmail("email@gmail.com");

            // Act
            customerService.signUp(customerEntity);

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
            assertThatThrownBy(() -> customerService.signUp(customerEntity))
                    .isInstanceOf(AlreadyExistsException.class)
                    .hasMessage("Customer with this email: email@gmail.com already exists");
        }

    }

}
