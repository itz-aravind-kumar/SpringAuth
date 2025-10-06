package com.example.second.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation used on password fields to enforce password policy.
 */
@Documented
@Constraint(validatedBy = com.example.second.validation.validator.PasswordConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "{validation.password.invalid}";
    int min() default 8;
    int max() default 128;
    boolean requireUppercase() default true;
    boolean requireLowercase() default true;
    boolean requireDigit() default true;
    boolean requireSpecial() default true;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}