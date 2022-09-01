package com.tourism.travels.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PresentOrFutureDateValidator.class})
public @interface PresentOrFutureDateCheck {

    String message() default "must be a date in the present or in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
