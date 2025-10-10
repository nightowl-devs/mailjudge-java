package dev.nightowl.mailjudge;

import dev.nightowl.mailjudge.rules.Rulesets;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MailJudgeTest {

    @Test
    void testValidEmails() {
        assertThat(MailJudge.verify("test@example.com").valid()).isTrue();
        assertThat(MailJudge.verify("user.name@example.com").valid()).isTrue();
        assertThat(MailJudge.verify("user+tag@example.co.uk").valid()).isTrue();
        assertThat(MailJudge.verify("user_name@sub.example.com").valid()).isTrue();
        assertThat(MailJudge.verify("123@example.com").valid()).isTrue();
    }

    @Test
    void testInvalidEmails() {
        assertThat(MailJudge.verify("").valid()).isFalse();
        assertThat(MailJudge.verify("notanemail").valid()).isFalse();
        assertThat(MailJudge.verify("@example.com").valid()).isFalse();
        assertThat(MailJudge.verify("user@").valid()).isFalse();
        assertThat(MailJudge.verify("user@@example.com").valid()).isFalse();
        assertThat(MailJudge.verify("user@example").valid()).isFalse();
        assertThat(MailJudge.verify("user..name@example.com").valid()).isFalse();
        assertThat(MailJudge.verify(".user@example.com").valid()).isFalse();
        assertThat(MailJudge.verify("user.@example.com").valid()).isFalse();
    }

    @Test
    void testNullAndEmpty() {
        ValidationResult nullResult = MailJudge.verify(null);
        assertThat(nullResult.valid()).isFalse();
        assertThat(nullResult.errors()).isNotEmpty();

        ValidationResult emptyResult = MailJudge.verify("");
        assertThat(emptyResult.valid()).isFalse();
        assertThat(emptyResult.errors()).isNotEmpty();
    }

    @Test
    void testCustomRuleset() {
        MailJudge standardJudge = MailJudge.withRuleset(Rulesets.standard());
        assertThat(standardJudge.isValid("user@example.com")).isTrue();
    }

    @Test
    void testStandardRuleset() {
        MailJudge judge = MailJudge.withRuleset(Rulesets.standard());
        assertThat(judge.isValid("test@example.com")).isTrue();
        assertThat(judge.isValid("test@example")).isFalse();
    }

    @Test
    void testNoDisposableRuleset() {
        MailJudge judge = MailJudge.withRuleset(Rulesets.noDisposable());
        assertThat(judge.isValid("test@example.com")).isTrue();
        assertThat(judge.isValid("test@mailinator.com")).isFalse();
        assertThat(judge.isValid("test@10minutemail.com")).isFalse();
    }

    @Test
    void testValidationResultDetails() {
        ValidationResult result = MailJudge.verify("invalid..email@example.com");
        assertThat(result.valid()).isFalse();
        assertThat(result.email()).isEqualTo("invalid..email@example.com");
        assertThat(result.errors()).isNotEmpty();
    }

    @Test
    void testLongEmails() {
        String longLocal = "a".repeat(65) + "@example.com";
        assertThat(MailJudge.verify(longLocal).valid()).isFalse();

        String validLongLocal = "a".repeat(64) + "@example.com";
        assertThat(MailJudge.verify(validLongLocal).valid()).isTrue();
    }

    @Test
    void testSpecialCharacters() {
        assertThat(MailJudge.verify("user+tag@example.com").valid()).isTrue();
        assertThat(MailJudge.verify("user_name@example.com").valid()).isTrue();
        assertThat(MailJudge.verify("user-name@example.com").valid()).isTrue();
        assertThat(MailJudge.verify("user!name@example.com").valid()).isFalse();
        assertThat(MailJudge.verify("user#name@example.com").valid()).isFalse();
    }

    @Test
    void testDomainValidation() {
        assertThat(MailJudge.verify("user@-example.com").valid()).isFalse();
        assertThat(MailJudge.verify("user@example-.com").valid()).isFalse();
        assertThat(MailJudge.verify("user@example..com").valid()).isFalse();
        assertThat(MailJudge.verify("user@.example.com").valid()).isFalse();
        assertThat(MailJudge.verify("user@example.com.").valid()).isFalse();
    }

    @Test
    void testTldValidation() {
        assertThat(MailJudge.verify("user@example.c").valid()).isFalse();
        assertThat(MailJudge.verify("user@example.co").valid()).isTrue();
        assertThat(MailJudge.verify("user@example.com").valid()).isTrue();
        assertThat(MailJudge.verify("user@example.museum").valid()).isTrue();
    }
}
