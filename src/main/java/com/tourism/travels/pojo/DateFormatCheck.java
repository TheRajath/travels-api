package com.tourism.travels.pojo;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {DateValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormatCheck {

    String message() default "travel date is in wrong format, correct format is yyyy-mm-dd";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
