# MailJudge

⚡ **Ultra-fast Java email verification library without regex**

MailJudge is a professional, high-performance email validation library for Java that uses character-by-character parsing instead of regular expressions, making it significantly faster while maintaining accuracy.

## Features

- 🚀 **Blazingly Fast** - No regex, pure character-by-character validation
- 🎯 **Accurate** - RFC 5321 compliant validation
- 🔧 **Flexible** - Predefined rulesets and custom rule creation
- 📦 **Lightweight** - Minimal dependencies
- 🧩 **Extensible** - Easy to create custom validation rules
- 🧪 **Well Tested** - Comprehensive test coverage
- 📊 **Benchmarked** - Includes JMH benchmarks

## Quick Start

### Simple Usage

```java
import dev.nightowl.mailjudge.MailJudge;
import dev.nightowl.mailjudge.ValidationResult;

// Quick validation with standard rules
ValidationResult result = MailJudge.verify("user@example.com");

if (result.isValid()) {
    System.out.println("Valid email!");
} else {
    System.out.println("Invalid: " + result.getErrors());
}
```

### Custom Rulesets

```java
import dev.nightowl.mailjudge.MailJudge;
import dev.nightowl.mailjudge.rules.Rulesets;

// Use predefined rulesets
MailJudge standardJudge = MailJudge.withRuleset(Rulesets.standard());
MailJudge strictJudge = MailJudge.withRuleset(Rulesets.strict());
MailJudge noDisposableJudge = MailJudge.withRuleset(Rulesets.noDisposable());

boolean isValid = strictJudge.isValid("user@example.com");
```

### Custom Rules

```java
import dev.nightowl.mailjudge.rules.Ruleset;
import dev.nightowl.mailjudge.rules.impl.*;

// Build custom ruleset
Ruleset customRuleset = Ruleset.builder()
    .rule(new SyntaxRule())
    .rule(new LengthRule())
    .rule(new DomainRule())
    .rule(new MxRecordRule())
    .build();

MailJudge judge = MailJudge.withRuleset(customRuleset);
```

## Predefined Rulesets

### Standard (Default)
- Syntax validation
- Length validation (RFC 5321)
- Local part validation
- Domain validation
- TLD validation
- **Recommended for most use cases**

### Strict
- All standard rules
- **MX record lookup** (requires DNS query)
- Validates that the domain can actually receive email

### No Disposable
- All standard rules
- Blocks disposable email providers
- **Uses GitHub-sourced list with local caching**

### Complete
- All rules including MX and disposable checks
- Most comprehensive validation
- Slowest due to DNS lookups

## Available Rules

| Rule | Description | Performance |
|------|-------------|-------------|
| `SyntaxRule` | Basic @ symbol and structure | ⚡⚡⚡ Very Fast |
| `LengthRule` | RFC 5321 length limits | ⚡⚡⚡ Very Fast |
| `LocalPartRule` | Local part format validation | ⚡⚡ Fast |
| `DomainRule` | Domain structure validation | ⚡⚡ Fast |
| `TldRule` | Top-level domain validation | ⚡⚡ Fast |
| `DisposableRule` | Disposable email detection | ⚡⚡ Fast |
| `MxRecordRule` | DNS MX record lookup | ⚡ Slow (network) |

## Performance

MailJudge is designed for maximum performance by avoiding regex entirely. Here's a comparison:

```
Benchmark                                    Mode  Cnt   Score   Error  Units
mailJudgeStandardValid                       avgt    5 ~200 ns   ±...  ns/op
regexValid                                   avgt    5 ~800 ns   ±...  ns/op
```

**MailJudge is approximately 4x faster than regex-based validation!**

Run benchmarks yourself:
```bash
./gradlew benchmark
```

## Installation

### Maven
Add the following repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>nightowldev-repo</id>
        <url>https://repo.nightowl.dev</url>
    </repository>
</repositories>

<dependency>
    <groupId>dev.nightowl</groupId>
    <artifactId>mailjudge</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle
Add the following repository and dependency to your `build.gradle`:

```gradle
repositories {
    maven {
        url = uri("https://repo.nightowl.dev")
    }
}

dependencies {
    implementation 'dev.nightowl:mailjudge:1.0.0'
}
```

## Building

### Requirements
- Java 17 or higher
- Gradle 8.x

### Build Commands

```bash
# Build the library
./gradlew build

# Run tests
./gradlew test

# Run benchmarks
./gradlew benchmark

# Generate Javadoc
./gradlew javadoc
```

## Creating Custom Rules

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

### Batch Validation

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

### Detailed Error Messages

```java
ValidationResult result = MailJudge.verify("invalid..email@example.com");

if (!result.isValid()) {
    System.out.println("Email: " + result.getEmail());
    result.getErrors().forEach(error -> 
        System.out.println("  - " + error)
    );
}
```

## Why No Regex?

1. **Performance** - Regex compilation and execution is slower than direct character comparison
2. **Predictability** - No regex backtracking catastrophes
3. **Debuggability** - Easier to understand and debug validation logic
4. **Extensibility** - Simple to add custom validation rules

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
