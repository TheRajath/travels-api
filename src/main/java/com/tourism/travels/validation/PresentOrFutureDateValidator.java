package com.tourism.travels.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class PresentOrFutureDateValidator implements ConstraintValidator<PresentOrFutureDateCheck, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {

        if (date == null) {

            return true;
        }

        try {

            var todayDate = LocalDate.now();
            var localDate = LocalDate.parse(date);

            return todayDate.equals(localDate) || todayDate.isBefore(localDate);
        }

        catch (Exception e) {

            return true;
        }

    }

}
