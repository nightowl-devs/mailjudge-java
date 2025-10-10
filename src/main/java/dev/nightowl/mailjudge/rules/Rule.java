package dev.nightowl.mailjudge.rules;

/**
 * Base interface for email validation rules.
 */
public interface Rule {
    /**
     * Validates an email address according to this rule.
     *
     * @param email the email address to validate
     * @return true if the email passes this rule, false otherwise
     */
    boolean validate(String email);

    /**
     * Returns a descriptive error message when validation fails.
     *
     * @return the error message
     */
    String getErrorMessage();
}
