package dev.nightowl.mailjudge.rules;

/**
 * Predefined rule types for email validation.
 */
public enum RuleType {
    /**
     * Validates basic email syntax (local@domain format).
     */
    SYNTAX,

    /**
     * Validates email length constraints.
     */
    LENGTH,

    /**
     * Validates that the domain has valid MX records.
     */
    MX_RECORD,

    /**
     * Validates the domain format and structure.
     */
    DOMAIN,

    /**
     * Validates the local part (before @).
     */
    LOCAL_PART,

    /**
     * Checks against common disposable email providers.
     */
    DISPOSABLE,

    /**
     * Validates the top-level domain (TLD).
     */
    TLD
}
