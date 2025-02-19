package io.github.jtsato.walletservice.core.domains.transaction.usecase.deposit;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositCommand;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Deposit Command Test")
class DepositCommandTest {

    @DisplayName("Fail to create DepositCommand if parameters are null")
    @Test
    void failToCreateDepositCommandIfParametersAreNull() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new DepositCommand(null, null));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(2);
        assertThat(constraintViolationException.getMessage()).contains("walletId: validation.wallet.id.null");
        assertThat(constraintViolationException.getMessage()).contains("amount: validation.transaction.deposit.amount.null");
    }

    @DisplayName("Fail to create DepositCommand if parameters are invalid")
    @Test
    void failToCreateDepositCommandIfParametersAreInvalid() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new DepositCommand(0L, "0"));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(1);
        assertThat(constraintViolationException.getMessage()).contains("amount: validation.transaction.deposit.amount.invalid");
    }
}
