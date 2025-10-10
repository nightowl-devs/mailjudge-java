package dev.nightowl.mailjudge;

import dev.nightowl.mailjudge.rules.Rule;
import dev.nightowl.mailjudge.rules.Ruleset;
import dev.nightowl.mailjudge.rules.Rulesets;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point for email validation.
 * Fast, non-regex based email validation library.
 * 
 * Usage:
 * <pre>
 * // Quick validation with standard rules
 * ValidationResult result = MailJudge.verify("test@example.com");
 * 
 * // Custom ruleset
 * MailJudge judge = MailJudge.withRuleset(Rulesets.strict());
 * ValidationResult result = judge.verify("test@example.com");
 * </pre>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MailJudge {
    private static final MailJudge DEFAULT_INSTANCE = new MailJudge(Rulesets.standard());

    private final Ruleset ruleset;

    /**
     * Quick validation using standard ruleset.
     * 
     * @param email the email address to validate
     * @return validation result
     */
    public static ValidationResult verify(String email) {
        return DEFAULT_INSTANCE.validate(email);
    }

    /**
     * Creates a MailJudge instance with a custom ruleset.
     * 
     * @param ruleset the ruleset to use for validation
     * @return a MailJudge instance
     */
    public static MailJudge withRuleset(Ruleset ruleset) {
        return new MailJudge(ruleset);
    }

    /**
     * Validates an email address using this instance's ruleset.
     * 
     * @param email the email address to validate
     * @return validation result with detailed error information
     */
    public ValidationResult validate(String email) {
        if (email == null || email.isEmpty()) {
            return ValidationResult.invalid(email, "Email cannot be null or empty");
        }

        List<String> errors = new ArrayList<>();
        
        for (Rule rule : ruleset.getRules()) {
            if (!rule.validate(email)) {
                errors.add(rule.getErrorMessage());
            }
        }

        if (errors.isEmpty()) {
            return ValidationResult.valid(email);
        }

        return ValidationResult.invalid(email, errors);
    }

    /**
     * Quick check if an email is valid.
     * 
     * @param email the email address to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(String email) {
        return validate(email).valid();
    }
}
