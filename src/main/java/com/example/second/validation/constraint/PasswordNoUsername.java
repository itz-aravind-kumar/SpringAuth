package com.example.second.validation.constraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.example.second.validation.validator.PasswordNoUsernameValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordNoUsername {
    String message() default "{validation.password.contains.username}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}