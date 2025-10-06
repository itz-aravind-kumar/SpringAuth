package com.example.second.validation.validator;

import com.example.second.validation.constraint.PasswordNoUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class PasswordNoUsernameValidator implements ConstraintValidator<PasswordNoUsername, Object> {

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return true; // nothing to validate here

        try {
            // We expect the DTO to have fields named "username" and "password".
            Field usernameField = dto.getClass().getDeclaredField("username");
            Field passwordField = dto.getClass().getDeclaredField("password");

            usernameField.setAccessible(true);
            passwordField.setAccessible(true);

            Object usernameObj = usernameField.get(dto);
            Object passwordObj = passwordField.get(dto);

            // If username or password is missing, let other validators handle @NotBlank/@NotNull
            if (usernameObj == null || passwordObj == null) {
                return true;
            }

            String username = usernameObj.toString().toLowerCase();
            String password = passwordObj.toString().toLowerCase();

            if (password.contains(username)) {
                context.disableDefaultConstraintViolation();
                // Attach the violation to the password field for clearer error mapping
                context.buildConstraintViolationWithTemplate("{validation.password.contains.username}")
                       .addPropertyNode("password")
                       .addConstraintViolation();
                return false;
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            // If fields don't exist or are inaccessible, we choose to not fail validation here.
            // Alternatively, you could return false to fail fast.
            return true;
        }
    }
}
