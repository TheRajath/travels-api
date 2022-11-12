package com.tourism.travels.ticket;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.pojo.SearchRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.tourism.travels.sql.QTicketEntity.ticketEntity;

@Component
public class PredicateBuilder {

    public Predicate buildSearchPredicate(SearchRequest searchRequest) {

        var predicate = new BooleanBuilder();

        if (searchRequest.customerId() != null) {

            predicate.and(ticketEntity.customerId.eq(searchRequest.customerId()));
        }

        if (searchRequest.packageId() != null) {

            predicate.and(ticketEntity.packageId.eq(searchRequest.packageId()));
        }

        if (searchRequest.email() != null) {

            predicate.and(ticketEntity.customerEntity.email.eq(searchRequest.email()));
        }

        if (searchRequest.travelDate() != null) {

            predicate.and(ticketEntity.travelDate.eq(LocalDate.parse(searchRequest.travelDate())));
        }

        if (predicate.getValue() == null) {

            throw new BusinessValidationException("request body must contain at least one of the following search" +
                    " criteria: customerId, packageId, email, travelDate");
        }

        return predicate;
    }

}
