package io.github.jtsato.walletservice.core.domains.transactions.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TypeTest {

    @Test
    void exposesMessageKeys() {
        assertThat(Type.DEPOSIT.getMessageKey()).isEqualTo("enum-type-deposit");
        assertThat(Type.WITHDRAWAL.getMessageKey()).isEqualTo("enum-type-withdrawal");
        assertThat(Type.TRANSFER.getMessageKey()).isEqualTo("enum-type-transfer");
    }

    @Test
    void supportsMembershipPredicates() {
        assertThat(Type.DEPOSIT.is(Type.DEPOSIT)).isTrue();
        assertThat(Type.DEPOSIT.is(Type.TRANSFER)).isFalse();
        assertThat(Type.DEPOSIT.isNot(Type.TRANSFER)).isTrue();
        assertThat(Type.DEPOSIT.in(Type.TRANSFER, Type.DEPOSIT)).isTrue();
        assertThat(Type.DEPOSIT.notIn(Type.TRANSFER)).isTrue();
    }
}
