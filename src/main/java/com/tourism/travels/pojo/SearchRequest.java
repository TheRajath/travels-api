package com.tourism.travels.pojo;

import com.tourism.travels.validation.DateFormatCheck;
import com.tourism.travels.validation.NotEmptyIfPresent;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

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

}
