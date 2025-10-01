package com.example.second.validation.constraint;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.example.second.validation.validator.NameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    String message() default "{validation.name.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}