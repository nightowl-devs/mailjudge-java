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

        int dotIndex = domain.indexOf('.');
        if (dotIndex == -1) {
            return false;
        }

        if (domain.charAt(0) == '.' || domain.charAt(0) == '-' ||
            domain.charAt(domain.length() - 1) == '.' || domain.charAt(domain.length() - 1) == '-') {
            return false;
        }

        boolean previousWasDot = false;
        char previousChar = '\0';
        
        for (int i = 0; i < domain.length(); i++) {
            char c = domain.charAt(i);

            if (c == '.') {
                if (previousChar == '-') {
                    return false;
                }
                if (previousWasDot) {
                    return false;
                }
                previousWasDot = true;
            } else {
                if (previousWasDot && c == '-') {
                    return false;
                }
                previousWasDot = false;
                
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
