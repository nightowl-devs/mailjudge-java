package dev.nightowl.mailjudge;

import dev.nightowl.mailjudge.rules.Rulesets;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Benchmark comparing MailJudge  vs traditional regex approach.
 * 
 * Run with: ./gradlew benchmark
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 100, time = 1)
public class EmailValidationBenchmark {

    private static final String VALID_EMAIL = "user.name+tag@example.co.uk";
    private static final String INVALID_EMAIL = "invalid..email@@example..com";
    
    private static final Pattern REGEX_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private MailJudge standardJudge;

    @Setup
    public void setup() {
        standardJudge = MailJudge.withRuleset(Rulesets.standard());
    }

    // ========== MailJudge Benchmarks ==========

    @Benchmark
    public ValidationResult mailJudgeStandardValid() {
        return standardJudge.validateQ(VALID_EMAIL);
    }

    @Benchmark
    public ValidationResult mailJudgeStandardInvalid() {
        return standardJudge.validateQ(INVALID_EMAIL);
    }

    @Benchmark
    public ValidationResult mailJudgeStandardDetailedValid() {
        return standardJudge.validateQ(VALID_EMAIL);
    }

    @Benchmark
    public ValidationResult mailJudgeStandardDetailedInvalid() {
        return standardJudge.validateQ(INVALID_EMAIL);
    }

    // ========== Regex Benchmarks (for comparison) ==========

    @Benchmark
    public boolean regexValid() {
        return REGEX_PATTERN.matcher(VALID_EMAIL).matches();
    }

    @Benchmark
    public boolean regexInvalid() {
        return REGEX_PATTERN.matcher(INVALID_EMAIL).matches();
    }


}
