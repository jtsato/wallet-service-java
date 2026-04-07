package io.github.jtsato.walletservice.core.common;

import io.github.jtsato.walletservice.core.exception.InvalidEnumeratorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("Enumerator Utils Test")
class EnumeratorUtilsTest {

    private enum Color {
        RED, BLUE
    }

    @DisplayName("valueOf should return the enum constant")
    @Test
    void valueOfShouldReturnEnumConstant() {
        assertThat(EnumeratorUtils.valueOf("RED", Color.class))
                .isEqualTo(Color.RED);
    }

    @DisplayName("valueOf should throw InvalidEnumeratorException on unknown value")
    @Test
    void valueOfShouldThrowInvalidEnumeratorException() {
        final Throwable throwable = catchThrowable(() -> EnumeratorUtils.valueOf("GREEN", Color.class));

        assertThat(throwable)
                .isInstanceOf(InvalidEnumeratorException.class);

        final InvalidEnumeratorException ex = (InvalidEnumeratorException) throwable;
        assertThat(ex.getMessage()).isEqualTo("validation.enumerator.value.invalid");
        // EnumeratorUtils passes clazz.getSimpleName() (not the Class instance) as the 2nd argument
        assertThat(ex.getArgs()).containsExactly("GREEN", "Color", "RED, BLUE");
    }
}

