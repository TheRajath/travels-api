package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;
import com.tourism.travels.validation.NotEmptyIfPresent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static com.tourism.travels.pojo.SearchRequest.FieldName.TRAVEL_DATE;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Getter
@Setter
public class SearchRequest {

    @NotEmptyIfPresent
    private String customerId;

    @NotEmptyIfPresent
    private String packageId;

    @Email
    @NotEmptyIfPresent
    private String email;

    @DateFormatCheck
    private String travelDate;

    @Valid
    private Pagination pagination = new Pagination();

    @Valid
    private SortResultsBy sortResultsBy = new SortResultsBy();

    @Getter
    @Setter
    public static class SortResultsBy {

        private FieldName fieldName = TRAVEL_DATE;

        private Direction orderBy = ASC;

    }

    @Getter
    @RequiredArgsConstructor
    public enum FieldName {

        TRAVEL_DATE("travelDate"),
        CUSTOMER_ID("customerId"),
        CUSTOMER_NAME("customerEntity.firstName");

        private final String columnName;

    }

}
