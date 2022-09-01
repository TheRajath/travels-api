package com.tourism.travels.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatValidator implements ConstraintValidator<DateFormatCheck, String> {

    @Override
    public boolean isValid(String travelDateString, ConstraintValidatorContext constraintValidatorContext) {

        if (travelDateString == null) {

            return true;
        }

        try {
            var travelDate = LocalDate.parse(travelDateString);
            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return travelDateString.equals(dateTimeFormatter.format(travelDate));
        }

        catch (Exception e) {

            return false;
        }

    }

}
