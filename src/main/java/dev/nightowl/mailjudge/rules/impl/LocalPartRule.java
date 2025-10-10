package dev.nightowl.mailjudge.rules.impl;

import dev.nightowl.mailjudge.rules.Rule;

/**
 * Validates the local part of an email (before @) without regex.
 * Fast character-by-character validation.
 */
public class LocalPartRule implements Rule {

    @Override
    public boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return false;
        }

        String localPart = email.substring(0, atIndex);

        // Cannot start or end with dot
        if (localPart.charAt(0) == '.' || localPart.charAt(localPart.length() - 1) == '.') {
            return false;
        }

        boolean previousWasDot = false;
        for (int i = 0; i < localPart.length(); i++) {
            char c = localPart.charAt(i);

            // Check for consecutive dots
            if (c == '.') {
                if (previousWasDot) {
                    return false;
                }
                previousWasDot = true;
                continue;
            }
            previousWasDot = false;

            // Allowed characters: alphanumeric, dot, hyphen, underscore, plus
            if (!isValidLocalChar(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidLocalChar(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               (c >= '0' && c <= '9') ||
               c == '.' || c == '-' || c == '_' || c == '+';
    }

    @Override
    public String getErrorMessage() {
        return "Invalid local part format";
    }
}
