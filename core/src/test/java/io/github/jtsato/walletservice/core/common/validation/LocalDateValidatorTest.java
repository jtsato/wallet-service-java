package io.github.jtsato.walletservice.core.common.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateValidatorTest {

    private final LocalDateValidator validator = new LocalDateValidator();

    @Test
    void acceptsNullAndIsoDate() {
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid("2026-07-30", null)).isTrue();
    }

    @Test
    void rejectsInvalidDate() {
        assertThat(validator.isValid("30/07/2026", null)).isFalse();
    }
}
