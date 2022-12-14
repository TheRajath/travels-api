package com.tourism.travels.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.trimWhitespace;

public class NotEmptyIfPresentValidator implements ConstraintValidator<NotEmptyIfPresent, String> {

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {

        if (string == null) {

            return true;
        }

        return !trimWhitespace(string).isEmpty();
    }

}
