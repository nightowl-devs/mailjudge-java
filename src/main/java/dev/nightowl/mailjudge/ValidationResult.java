package dev.nightowl.mailjudge;

import lombok.Value;

import java.util.Collections;
import java.util.List;

/**
 * Immutable result of an email validation operation.
 */
public record ValidationResult(boolean valid, String email, List<String> errors) {
    /**
     * Creates a valid validation result.
     * 
     * @param email the valid email address
     * @return valid result
     */
    public static ValidationResult valid(String email) {
        return new ValidationResult(true, email, Collections.emptyList());
    }

    /**
     * Creates an invalid validation result with a single error.
     * 
     * @param email the invalid email address
     * @param error the error message
     * @return invalid result
     */
    public static ValidationResult invalid(String email, String error) {
        return new ValidationResult(false, email, Collections.singletonList(error));
    }

    /**
     * Creates an invalid validation result with multiple errors.
     * 
     * @param email the invalid email address
     * @param errors the list of error messages
     * @return invalid result
     */
    public static ValidationResult invalid(String email, List<String> errors) {
        return new ValidationResult(false, email, Collections.unmodifiableList(errors));
    }
}
