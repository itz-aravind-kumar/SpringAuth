package com.example.second.validation.validator;

import com.example.second.validation.constraint.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses Passay to enforce password rules based on the annotation configuration.
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private int min;
    private int max;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecial;

    private org.passay.PasswordValidator passayValidator;

    @Override
    public void initialize(ValidPassword annotation) {
        // read config from annotation
        this.min = annotation.min();
        this.max = annotation.max();
        this.requireUppercase = annotation.requireUppercase();
        this.requireLowercase = annotation.requireLowercase();
        this.requireDigit = annotation.requireDigit();
        this.requireSpecial = annotation.requireSpecial();

        // Build Passay rules. We'll add rules conditionally.
        List<Rule> rules = new ArrayList<>();
        rules.add(new LengthRule(min, max));    // enforce min/max length
        rules.add(new WhitespaceRule());        // disallow whitespace

        // require at least one uppercase
        if (requireUppercase) {
            rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        }
        // require at least one lowercase
        if (requireLowercase) {
            rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        }
        // require at least one digit
        if (requireDigit) {
            rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        }
        // require at least one special character (non-alphanumeric)
        if (requireSpecial) {
            rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
        }

        // You can add more rules here (dictionary, sequences, repeats, etc.)

        passayValidator = new org.passay.PasswordValidator(rules);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Null check: if null, fail validation (use @NotBlank or @NotNull on field if desired)
        if (password == null) {
            return false;
        }


        // Create a Passay PasswordData object and validate
        RuleResult result = passayValidator.validate(new PasswordData(password));


        if (result.isValid()) {
            return true;
        }

        // If not valid, disable default message and add specific Passay messages
        context.disableDefaultConstraintViolation();
        List<String> messages = passayValidator.getMessages(result);
        for (String msg : messages) {
            String key = mapPassayMessageToKey(msg); // convert Passay message → key
            context.buildConstraintViolationWithTemplate("{" + key + "}")
                .addConstraintViolation();
        }
        return false;
    }

    /**
     * Converts Passay's default English message into your message key.
     */
    private String mapPassayMessageToKey(String msg) {
        msg = msg.toLowerCase();

        if (msg.contains("uppercase")) {
            return "validation.password.requireUpper";
        }
        if (msg.contains("lowercase")) {
            return "validation.password.requireLower";
        }
        if (msg.contains("digit")) {
            return "validation.password.requireDigit";
        }
        if (msg.contains("special")) {
            return "validation.password.requireSpecial";
        }
        if (msg.contains("length")) {
            return "validation.password.length";
        }
        if (msg.contains("whitespace")) {
            return "validation.password.noWhitespace";
        }

        // Fallback — if none match
        return "validation.password.invalid";
    }

}
