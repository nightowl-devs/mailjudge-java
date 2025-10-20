# MailJudge

Fast, maintainable email validation for Java — without regular expressions

MailJudge provides reliable email validation using character-by-character parsing instead of regular expressions. This approach focuses on predictable performance, clear logic, and easy extensibility.

## Features

- Performance-focused validation using direct character parsing
- RFC 5321 checks where applicable
- Predefined rulesets and support for custom rules
- Lightweight with minimal dependencies
- Extensible: add custom validation rules easily
- Thoroughly unit tested

## Quick start

### Simple usage

```java
import dev.nightowl.mailjudge.MailJudge;
import dev.nightowl.mailjudge.ValidationResult;

// Validate an email with the standard ruleset
ValidationResult result = MailJudge.verify("user@example.com");

if (result.isValid()) {
    System.out.println("Valid email");
} else {
    System.out.println("Invalid: " + result.getErrors());
}
```

### Custom rulesets

```java
import dev.nightowl.mailjudge.MailJudge;
import dev.nightowl.mailjudge.rules.Rulesets;

// Use predefined rulesets
MailJudge standardJudge = MailJudge.withRuleset(Rulesets.standard());
MailJudge strictJudge = MailJudge.withRuleset(Rulesets.strict());
MailJudge noDisposableJudge = MailJudge.withRuleset(Rulesets.noDisposable());

boolean isValid = strictJudge.isValid("user@example.com");
```

### Custom rules

```java
import dev.nightowl.mailjudge.rules.Ruleset;
import dev.nightowl.mailjudge.rules.impl.*;

// Build a custom ruleset
Ruleset customRuleset = Ruleset.builder()
    .rule(new SyntaxRule())
    .rule(new LengthRule())
    .rule(new DomainRule())
    .rule(new MxRecordRule())
    .build();

MailJudge judge = MailJudge.withRuleset(customRuleset);
```

## Predefined rulesets

Standard (default)
- Syntax validation
- Length validation (RFC 5321)
- Local-part validation
- Domain validation
- TLD validation

Strict
- All standard rules
- Optional MX record lookup (requires DNS access)

No Disposable
- All standard rules
- Blocks known disposable email providers (local caching of the list)

Complete
- All rules including MX and disposable checks
- Most comprehensive validation

## Available rules

| Rule | Description |
|------|-------------|
| `SyntaxRule` | Basic `@` symbol and structure checks |
| `LengthRule` | RFC 5321 length limits |
| `LocalPartRule` | Local-part format validation |
| `DomainRule` | Domain structure validation |
| `TldRule` | Top-level domain validation |
| `DisposableRule` | Disposable email detection |
| `MxRecordRule` | DNS MX record lookup (network-dependent) |

## Building and running

### Requirements
- Java 17 or higher
- Gradle 8.x

### Common tasks

```bash
# Build the library
./gradlew build

# Run tests
./gradlew test

# Generate Javadoc
./gradlew javadoc
```

## Creating custom rules

Implement the `Rule` interface:

```java
import dev.nightowl.mailjudge.rules.Rule;

public class CustomRule implements Rule {
    @Override
    public boolean validate(String email) {
        // Your validation logic
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "Custom validation failed";
    }
}
```

## Examples

### Batch validation

```java
List<String> emails = Arrays.asList(
    "user1@example.com",
    "user2@example.com",
    "invalid@"
);

MailJudge judge = MailJudge.withRuleset(Rulesets.standard());

Map<String, ValidationResult> results = emails.stream()
    .collect(Collectors.toMap(
        email -> email,
        judge::validate
    ));
```

### Detailed error messages

```java
ValidationResult result = MailJudge.verify("invalid..email@example.com");

if (!result.isValid()) {
    System.out.println("Email: " + result.getEmail());
    result.getErrors().forEach(error -> 
        System.out.println("  - " + error)
    );
}
```

## Why not use regular expressions?

- Better performance in many cases by avoiding regex compilation and backtracking
- Clearer, more maintainable parsing logic
- Easier to extend and reason about for custom rules

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome. Please open an issue or submit a pull request with a clear description of the change and tests where appropriate.
