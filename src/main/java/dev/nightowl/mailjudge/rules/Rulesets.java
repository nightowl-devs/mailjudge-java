package dev.nightowl.mailjudge.rules;

import dev.nightowl.mailjudge.rules.impl.*;

/**
 * Predefined rulesets for common validation scenarios.
 */
public final class Rulesets {

    private Rulesets() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Standard validation with syntax, length, local part, domain, and TLD checks.
     * Recommended for most use cases. Fast and thorough.
     * 
     * @return standard ruleset
     */
    public static Ruleset standard() {
        return Ruleset.builder()
                .rule(new SyntaxRule())
                .rule(new LengthRule())
                .rule(new LocalPartRule())
                .rule(new DomainRule())
                .rule(new TldRule())
                .build();
    }

    /**
     * Strict validation including MX record lookup.
     * Slower due to DNS lookup but provides higher confidence.
     * Validates that the domain can actually receive email.
     * 
     * @return strict ruleset with MX validation
     */
    public static Ruleset strict() {
        return Ruleset.builder()
                .rule(new SyntaxRule())
                .rule(new LengthRule())
                .rule(new LocalPartRule())
                .rule(new DomainRule())
                .rule(new TldRule())
                .rule(new MxRecordRule())
                .build();
    }

    /**
     * Standard validation plus disposable email detection.
     * Uses GitHub-sourced list of disposable domains with local caching.
     * 
     * @return ruleset that blocks disposable emails
     */
    public static Ruleset noDisposable() {
        return Ruleset.builder()
                .rule(new SyntaxRule())
                .rule(new LengthRule())
                .rule(new LocalPartRule())
                .rule(new DomainRule())
                .rule(new TldRule())
                .rule(new DisposableRule())
                .build();
    }

    /**
     * Complete validation with all rules including MX and disposable checks.
     * Most comprehensive but slowest due to DNS lookups.
     * 
     * @return complete ruleset with all validations
     */
    public static Ruleset complete() {
        return Ruleset.builder()
                .rule(new SyntaxRule())
                .rule(new LengthRule())
                .rule(new LocalPartRule())
                .rule(new DomainRule())
                .rule(new TldRule())
                .rule(new DisposableRule())
                .rule(new MxRecordRule())
                .build();
    }
}
