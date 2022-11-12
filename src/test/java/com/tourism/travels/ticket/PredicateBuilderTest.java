package com.tourism.travels.ticket;

import com.querydsl.core.BooleanBuilder;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.pojo.SearchRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.tourism.travels.sql.QTicketEntity.ticketEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PredicateBuilderTest {

    private final PredicateBuilder predicateBuilder = new PredicateBuilder();

    @Nested
    class BuildSearchPredicate {

        @Test
        void setsCustomerId_whenRequestContainsCustomerId() {
            // Arrange
            var searchRequest = new SearchRequest("123", null, null, null);

            var expectedPredicate = new BooleanBuilder();
            expectedPredicate.and(ticketEntity.customerId.eq("123"));

            // Act
            var returnedPredicate = predicateBuilder.buildSearchPredicate(searchRequest);

            // Assert
            assertThat(returnedPredicate).isEqualTo(expectedPredicate);
        }

        @Test
        void setsPackageId_whenRequestContainsPackageId() {
            // Arrange
            var searchRequest = new SearchRequest(null, "123", null, null);

            var expectedPredicate = new BooleanBuilder();
            expectedPredicate.and(ticketEntity.packageId.eq("123"));

            // Act
            var returnedPredicate = predicateBuilder.buildSearchPredicate(searchRequest);

            // Assert
            assertThat(returnedPredicate).isEqualTo(expectedPredicate);
        }

        @Test
        void setsEmail_whenRequestContainsEmail() {
            // Arrange
            var searchRequest = new SearchRequest(null, null, "sai@gmail.com", null);

            var expectedPredicate = new BooleanBuilder();
            expectedPredicate.and(ticketEntity.customerEntity.email.eq("sai@gmail.com"));

            // Act
            var returnedPredicate = predicateBuilder.buildSearchPredicate(searchRequest);

            // Assert
            assertThat(returnedPredicate).isEqualTo(expectedPredicate);
        }

        @Test
        void setsTravelDate_whenRequestContainsTravelDate() {
            // Arrange
            var searchRequest = new SearchRequest(null, null, null, "2022-12-15");

            var expectedPredicate = new BooleanBuilder();
            expectedPredicate.and(ticketEntity.travelDate.eq(LocalDate.parse("2022-12-15")));

            // Act
            var returnedPredicate = predicateBuilder.buildSearchPredicate(searchRequest);

            // Assert
            assertThat(returnedPredicate).isEqualTo(expectedPredicate);
        }

        @Test
        void throwsBusinessValidationException_whenRequestBodyIsNull() {
            // Arrange
            var searchRequest = new SearchRequest(null, null, null, null);

            // Act/Assert
            assertThatThrownBy(() -> predicateBuilder.buildSearchPredicate(searchRequest))
                    .isInstanceOf(BusinessValidationException.class)
                    .hasMessage("request body must contain at least one of the following search" +
                            " criteria: customerId, packageId, email, travelDate");
        }

    }

}
