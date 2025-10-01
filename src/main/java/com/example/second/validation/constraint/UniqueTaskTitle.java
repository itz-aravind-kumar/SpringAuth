package com.example.second.validation.constraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import com.example.second.validation.validator.UniqueTaskTitleValidator;

@Documented
@Constraint(validatedBy = UniqueTaskTitleValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTaskTitle {
    String message() default "Task title must be unique for this user";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
