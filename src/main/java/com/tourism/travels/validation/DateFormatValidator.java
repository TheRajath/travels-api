package com.tourism.travels.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatValidator implements ConstraintValidator<DateFormatCheck, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {

        if (date == null) {

            return true;
        }

        try {
            var localDate = LocalDate.parse(date);
            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            return date.equals(dateTimeFormatter.format(localDate));
        }

        catch (Exception e) {

            return false;
        }

    }

}
