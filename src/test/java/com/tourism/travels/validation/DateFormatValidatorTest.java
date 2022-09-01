package com.tourism.travels.validation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DateFormatValidatorTest {

    private final DateFormatValidator dateFormatValidator = new DateFormatValidator();

    @Nested
    class IsValid {

        @Test
        void returnsTrue_whenTravelDateIsNull() {
            // Act
            var valid = dateFormatValidator.isValid(null, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsTrue_whenTravelDateIsInProperFormat() {
            // Arrange
            var date = "2020-12-01";

            // Act
            var valid = dateFormatValidator.isValid(date, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsFalse_whenTravelDateIsInImProperFormat() {
            // Arrange
            var date = "date";

            // Act
            var valid = dateFormatValidator.isValid(date, null);

            // Assert
            assertThat(valid).isFalse();
        }

    }

}
