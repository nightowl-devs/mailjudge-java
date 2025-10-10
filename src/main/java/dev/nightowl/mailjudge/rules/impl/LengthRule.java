package dev.nightowl.mailjudge.rules.impl;

import dev.nightowl.mailjudge.rules.Rule;
import lombok.AllArgsConstructor;

/**
 * Validates email length according to RFC 5321.
 * Maximum local part: 64 characters
 * Maximum domain: 255 characters
 * Maximum total: 320 characters
 */
@AllArgsConstructor
public class LengthRule implements Rule {
    private final int maxLength;
    private final int maxLocalLength;
    private final int maxDomainLength;

    /**
     * Creates a new LengthRule with RFC 5321 defaults.
     */
    public LengthRule() {
        this(320, 64, 255);
    }

    @Override
    public boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        if (email.length() > maxLength) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return false;
        }

        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        return localPart.length() <= maxLocalLength && 
               domain.length() <= maxDomainLength;
    }

    @Override
    public String getErrorMessage() {
        return "Email exceeds maximum length";
    }
}
