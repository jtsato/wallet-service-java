package io.github.jtsato.walletservice.core.domains.transaction.usecase.withdraw;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawCommand;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WithDraw Command Test")
class WithDrawCommandTest {

    @DisplayName("Fail to create WithDrawCommand if parameters are null")
    @Test
    void failToCreateWithDrawCommandIfParametersAreNull() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new WithdrawCommand(null, null));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(2);
        assertThat(constraintViolationException.getMessage()).contains("walletId: validation.wallet.id.null");
        assertThat(constraintViolationException.getMessage()).contains("amount: validation.transaction.withdraw.amount.null");
    }

    @DisplayName("Fail to create WithDrawCommand if parameters are invalid")
    @Test
    void failToCreateWithDrawCommandIfParametersAreInvalid() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new WithdrawCommand(0L, BigDecimal.valueOf(0)));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(1);
        assertThat(constraintViolationException.getMessage()).contains("amount: validation.transaction.withdraw.amount.invalid");
    }
}
