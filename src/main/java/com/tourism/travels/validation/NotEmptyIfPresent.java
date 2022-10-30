package com.tourism.travels.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotEmptyIfPresentValidator.class})
public @interface NotEmptyIfPresent {

    String message() default "must not be empty if present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
