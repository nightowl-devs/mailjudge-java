package dev.nightowl.mailjudge.rules.impl;

import dev.nightowl.mailjudge.rules.Rule;

/**
 * Fast syntax validation.
 * Validates if email has the general syntax: local@domain
 */
public class SyntaxRule implements Rule {

    @Override
    public boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf('@');
        
        if (atIndex == -1 || atIndex != email.lastIndexOf('@')) {
            return false;
        }

        if (atIndex == 0) {
            return false;
        }

        if (atIndex == email.length() - 1) {
            return false;
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return "Invalid email syntax";
    }
}
