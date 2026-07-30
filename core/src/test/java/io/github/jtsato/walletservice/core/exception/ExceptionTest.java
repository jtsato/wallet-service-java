package io.github.jtsato.walletservice.core.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void preservesMessageAndArguments() {
        final InvalidActionException exception = new InvalidActionException("error.key", "value");

        assertThat(exception.getMessage()).isEqualTo("error.key");
        assertThat(exception.getArgs()).containsExactly("value");
    }

    @Test
    void supportsMessageWithoutArguments() {
        final CoreException exception = new CoreException("message");

        assertThat(exception.getMessage()).isEqualTo("message");
        assertThat(exception.getArgs()).isEmpty();
    }
}
