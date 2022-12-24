package com.tourism.travels.validation;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresentOrFutureDateValidatorTest {

    @Mock
    private ConstraintViolationBuilder builder;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private final PresentOrFutureDateValidator presentOrFutureDateValidator = new PresentOrFutureDateValidator();

    @Nested
    class IsValid {

        @Test
        void returnsTrue_whenDateIsNull() {
            // Act
            var valid = presentOrFutureDateValidator.isValid(null, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsTrue_whenDateIsEqualToTodayDate() {
            // Arrange
            var date = LocalDate.now().toString();

            // Act
            var valid = presentOrFutureDateValidator.isValid(date, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsTrue_whenDateIsInFuture() {
            // Arrange
            var date = "2099-01-01";

            // Act
            var valid = presentOrFutureDateValidator.isValid(date, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsFalse_whenDateIsInPast() {
            // Arrange
            var date = "2000-01-01";

            // Act
            var valid = presentOrFutureDateValidator.isValid(date, null);

            // Assert
            assertThat(valid).isFalse();
        }

        @Test
        void returnsFalse_whenDateIsNotIsCorrectFormat() {
            // Arrange
            var date = "date";

            when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);

            // Act
            var valid = presentOrFutureDateValidator.isValid(date, constraintValidatorContext);

            // Assert
            assertThat(valid).isFalse();

            verify(constraintValidatorContext).disableDefaultConstraintViolation();
            verify(constraintValidatorContext).buildConstraintViolationWithTemplate(anyString());

            verifyNoMoreInteractions(constraintValidatorContext);
        }

    }

}
