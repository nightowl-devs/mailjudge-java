package dev.nightowl.mailjudge.rules;

import dev.nightowl.mailjudge.rules.impl.DomainRule;
import dev.nightowl.mailjudge.rules.impl.LocalPartRule;
import dev.nightowl.mailjudge.rules.impl.SyntaxRule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RulesetTest {

    @Test
    void testRulesetBuilder() {
        Ruleset ruleset = Ruleset.builder()
                .rule(new SyntaxRule())
                .rule(new LocalPartRule())
                .rule(new DomainRule())
                .build();

        assertThat(ruleset.getRules()).hasSize(3);
        assertThat(ruleset.validate("test@example.com")).isTrue();
        assertThat(ruleset.validate("invalid")).isFalse();
    }

    @Test
    void testEmptyRuleset() {
        Ruleset ruleset = Ruleset.builder().build();
        assertThat(ruleset.getRules()).isEmpty();
        assertThat(ruleset.validate("anything")).isTrue();
    }

    @Test
    void testPredefinedRulesets() {
        assertThat(Rulesets.standard().getRules()).isNotEmpty();
        assertThat(Rulesets.strict().getRules()).isNotEmpty();
        assertThat(Rulesets.noDisposable().getRules()).isNotEmpty();
        assertThat(Rulesets.complete().getRules()).isNotEmpty();
    }

    @Test
    void testRulesetValidation() {
        Ruleset standard = Rulesets.standard();
        
        assertThat(standard.validate("user@example.com")).isTrue();
        assertThat(standard.validate("invalid")).isFalse();
        assertThat(standard.validate("user..name@example.com")).isFalse();
    }
}
