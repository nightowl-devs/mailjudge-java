![mailjudge-java](https://socialify.git.ci/nightowl-devs/mailjudge-java/image?description=1&font=Inter&issues=1&language=1&name=1&owner=1&pulls=1&stargazers=1&theme=Dark)

# MailJudge

Fast email validation for Java without regular expressions.

MailJudge validates email addresses using character by character parsing. It supports predefined rulesets and custom validation rules.

## Features

- Character-by-character email validation
- RFC 5321 length and syntax checks
- Predefined and custom rulesets
- Optional MX record validation
- Disposable email detection

## Quick start

```java
import dev.nightowl.mailjudge.MailJudge;
import dev.nightowl.mailjudge.ValidationResult;

ValidationResult result = MailJudge.verify("user@example.com");

if (result.isValid()) {
    System.out.println("Valid email");
} else {
    System.out.println("Invalid: " + result.getErrors());
}
```

### Rulesets

```java
import dev.nightowl.mailjudge.MailJudge;
import dev.nightowl.mailjudge.rules.Rulesets;

MailJudge standard = MailJudge.withRuleset(Rulesets.standard());
MailJudge strict = MailJudge.withRuleset(Rulesets.strict());
MailJudge noDisposable = MailJudge.withRuleset(Rulesets.noDisposable());

boolean valid = strict.isValid("user@example.com");
```

### Custom rules

```java
import dev.nightowl.mailjudge.MailJudge;
import dev.nightowl.mailjudge.rules.Ruleset;
import dev.nightowl.mailjudge.rules.impl.*;

Ruleset ruleset = Ruleset.builder()
    .rule(new SyntaxRule())
    .rule(new LengthRule())
    .rule(new DomainRule())
    .rule(new MxRecordRule())
    .build();

MailJudge judge = MailJudge.withRuleset(ruleset);
```

## Predefined rulesets

| Ruleset | Includes |
|---------|----------|
| `standard` | Syntax, length, local-part, domain, and TLD checks |
| `strict` | Standard rules with optional MX lookup |
| `noDisposable` | Standard rules with disposable email detection |
| `complete (RECOMMENDED)` | All available rules |

## Available rules

| Rule | Description |
|------|-------------|
| `SyntaxRule` | Basic email structure |
| `LengthRule` | RFC 5321 length limits |
| `LocalPartRule` | Local-part validation |
| `DomainRule` | Domain validation |
| `TldRule` | TLD validation |
| `DisposableRule` | Disposable email detection |
| `MxRecordRule` | MX record lookup |

## Build

Requires Java 17+ and Gradle 8.x.

```bash
./gradlew build
./gradlew test
./gradlew javadoc
```

## Custom rules

Implement the `Rule` interface:

```java
import dev.nightowl.mailjudge.rules.Rule;

public class CustomRule implements Rule {
    @Override
    public boolean validate(String email) {
        return true;
    }

    @Override
    public String getErrorMessage() {
        return "Custom validation failed";
    }
}
```

## License

MIT. See [LICENSE](LICENSE).
