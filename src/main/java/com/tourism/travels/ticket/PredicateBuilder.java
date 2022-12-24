package com.tourism.travels.ticket;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.tourism.travels.exception.BusinessValidationException;
import com.tourism.travels.pojo.SearchRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.tourism.travels.sql.QTicketEntity.ticketEntity;
import static java.lang.Integer.parseInt;

@Component
public class PredicateBuilder {

    public Predicate buildSearchPredicate(SearchRequest searchRequest) {

        var predicate = new BooleanBuilder();

        if (searchRequest.getCustomerId() != null) {

            predicate.and(ticketEntity.customerId.eq(parseInt(searchRequest.getCustomerId())));
        }

        if (searchRequest.getPackageId() != null) {

            predicate.and(ticketEntity.packageId.eq(parseInt(searchRequest.getPackageId())));
        }

        if (searchRequest.getEmail() != null) {

            predicate.and(ticketEntity.customerEntity.email.eq(searchRequest.getEmail()));
        }

        if (searchRequest.getTravelDate() != null) {

            predicate.and(ticketEntity.travelDate.eq(LocalDate.parse(searchRequest.getTravelDate())));
        }

        if (predicate.getValue() == null) {

            throw new BusinessValidationException("request body must contain at least one of the following search" +
                    " criteria: customerId, packageId, email, travelDate");
        }

        return predicate;
    }

}
