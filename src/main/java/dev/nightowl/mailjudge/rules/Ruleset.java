package dev.nightowl.mailjudge.rules;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

/**
 * A collection of validation rules to be applied to email addresses.
 */
@Getter
@Builder
public class Ruleset {
    @Singular
    private final List<Rule> rules;

    /**
     * Validates an email against all rules in this ruleset.
     *
     * @param email the email address to validate
     * @return true if the email passes all rules, false otherwise
     */
    public boolean validate(String email) {
        for (Rule rule : rules) {
            if (!rule.validate(email)) {
                return false;
            }
        }
        return true;
    }
}
