package com.tourism.travels.validation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PresentOrFutureDateValidatorTest {

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
        void returnsTrue_whenDateIsNotIsCorrectFormat() {
            // Arrange
            var date = "date";

            // Act
            var valid = presentOrFutureDateValidator.isValid(date, null);

            // Assert
            assertThat(valid).isTrue();
        }

    }

}
