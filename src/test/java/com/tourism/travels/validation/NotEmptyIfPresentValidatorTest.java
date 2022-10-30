package com.tourism.travels.validation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class NotEmptyIfPresentValidatorTest {

    private final NotEmptyIfPresentValidator notEmptyIfPresentValidator = new NotEmptyIfPresentValidator();

    @Nested
    class IsValid {

        @Test
        void returnsTrue_whenStringIsNull() {
            // Act
            var valid = notEmptyIfPresentValidator.isValid(null, null);

            // Assert
            assertThat(valid).isTrue();
        }

        @Test
        void returnsTrue_whenStringIsNotEmpty() {
            // Act
            var valid = notEmptyIfPresentValidator.isValid("String", null);

            // Assert
            assertThat(valid).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "  "})
        void returnsFalse_whenStringIsEmpty(String string) {
            // Act
            var valid = notEmptyIfPresentValidator.isValid(string, null);

            // Assert
            assertThat(valid).isFalse();
        }

    }

}
