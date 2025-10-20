package dev.nightowl.mailjudge.rules.impl;

import dev.nightowl.mailjudge.rules.Rule;

/**
 * Validates the top-level domain (TLD).
 * Ensures TLD is at least 2 characters and contains only letters.
 */
public class TldRule implements Rule {
    private final int minTldLength;

    /**
     * Creates a new TldRule with minimum TLD length of 2.
     */
    public TldRule() {
        this(2);
    }

    /**
     * Creates a new TldRule with custom minimum TLD length.
     * 
     * @param minTldLength minimum TLD length
     */
    public TldRule(int minTldLength) {
        this.minTldLength = minTldLength;
    }

    @Override
    public boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return false;
        }

        String domain = email.substring(atIndex + 1);
        int lastDotIndex = domain.lastIndexOf('.');
        
        if (lastDotIndex == -1 || lastDotIndex == domain.length() - 1) {
            return false;
        }

        String tld = domain.substring(lastDotIndex + 1);

        if (tld.length() < minTldLength) {
            return false;
        }

        // TLD must contain only letters
        for (int i = 0; i < tld.length(); i++) {
            char c = tld.charAt(i);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return "Invalid top-level domain";
    }
}
