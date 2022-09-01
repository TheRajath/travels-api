package com.tourism.travels.validation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DateFormatValidatorTest {

    private final DateFormatValidator dateFormatValidator = new DateFormatValidator();

    @Nested
    class IsValid {

        @Test
        void returnsTrue_whenDateIsNull() {
            // Act
            var valid = dateFormatValidator.isValid(null, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsTrue_whenDateIsInProperFormat() {
            // Arrange
            var date = "2020-12-01";

            // Act
            var valid = dateFormatValidator.isValid(date, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsFalse_whenDateIsInImProperFormat() {
            // Arrange
            var date = "date";

            // Act
            var valid = dateFormatValidator.isValid(date, null);

            // Assert
            assertThat(valid).isFalse();
        }

    }

}
