package dev.nightowl.mailjudge.rules.impl;

import dev.nightowl.mailjudge.rules.Rule;
import dev.nightowl.mailjudge.util.DisposableEmailProvider;

import java.util.Set;

/**
 * Checks against disposable email providers.
 * Uses GitHub-sourced list with local caching for performance.
 * Fast hash-based lookup.
 */
public class DisposableRule implements Rule {
    private final Set<String> disposableDomains;

    /**
     * Creates a new DisposableRule with GitHub-sourced domains.
     */
    public DisposableRule() {
        this.disposableDomains = DisposableEmailProvider.getDisposableDomains();
    }

    /**
     * Creates a new DisposableRule with custom disposable domains.
     * 
     * @param customDisposableDomains set of domains to block
     */
    public DisposableRule(Set<String> customDisposableDomains) {
        this.disposableDomains = customDisposableDomains;
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

        String domain = email.substring(atIndex + 1).toLowerCase();
        return !disposableDomains.contains(domain);
    }

    @Override
    public String getErrorMessage() {
        return "Disposable email addresses are not allowed";
    }
}
