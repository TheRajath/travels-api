package com.tourism.travels.validation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresentOrFutureDateValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    private final PresentOrFutureDateValidator presentOrFutureDateValidator = new PresentOrFutureDateValidator();

    PresentOrFutureDateValidatorTest() {
    }

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
        }

    }

}