package io.github.jtsato.walletservice.core.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GetLocalDateTimeImplTest {

    @Test
    void returnsCurrentDateTime() {
        assertThat(new GetLocalDateTimeImpl().now()).isNotNull();
    }
}
