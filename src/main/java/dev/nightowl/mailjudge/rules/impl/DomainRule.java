package dev.nightowl.mailjudge.rules.impl;

import dev.nightowl.mailjudge.rules.Rule;

/**
 * Validates domain format without regex.
 * Fast character-by-character validation.
 */
public class DomainRule implements Rule {

    @Override
    public boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1 || atIndex == email.length() - 1) {
            return false;
        }

        String domain = email.substring(atIndex + 1);

        // Domain must contain at least one dot
        int dotIndex = domain.indexOf('.');
        if (dotIndex == -1) {
            return false;
        }

        // Cannot start or end with dot or hyphen
        if (domain.charAt(0) == '.' || domain.charAt(0) == '-' ||
            domain.charAt(domain.length() - 1) == '.' || domain.charAt(domain.length() - 1) == '-') {
            return false;
        }

        // Validate each label (part between dots)
        boolean previousWasDot = false;
        char previousChar = '\0';
        
        for (int i = 0; i < domain.length(); i++) {
            char c = domain.charAt(i);

            if (c == '.') {
                // Check if previous character before dot was hyphen
                if (previousChar == '-') {
                    return false;
                }
                if (previousWasDot) {
                    return false;
                }
                previousWasDot = true;
            } else {
                // Check if character after dot is hyphen
                if (previousWasDot && c == '-') {
                    return false;
                }
                previousWasDot = false;
                
                // Allowed characters: alphanumeric, dot, hyphen
                if (!isValidDomainChar(c)) {
                    return false;
                }
            }
            
            previousChar = c;
        }

        return true;
    }

    private boolean isValidDomainChar(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               (c >= '0' && c <= '9') ||
               c == '.' || c == '-';
    }

    @Override
    public String getErrorMessage() {
        return "Invalid domain format";
    }
}
