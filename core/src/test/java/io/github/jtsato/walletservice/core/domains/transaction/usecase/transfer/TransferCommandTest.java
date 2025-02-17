package io.github.jtsato.walletservice.core.domains.transaction.usecase.transfer;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferCommand;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Transfer Command Test")
class TransferCommandTest {

    @DisplayName("Fail to create TransferCommand if parameters are null")
    @Test
    void failToCreateTransferCommandIfParametersAreNull() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new TransferCommand(null, null, null));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(3);
        assertThat(constraintViolationException.getMessage()).contains("sourceWalletId: validation.source.wallet.id.null");
        assertThat(constraintViolationException.getMessage()).contains("destinationWalletId: validation.destination.wallet.id.null");
        assertThat(constraintViolationException.getMessage()).contains("amount: validation.transaction.transfer.amount.null");
    }

    @DisplayName("Fail to create TransferCommand if parameters are invalid")
    @Test
    void failToCreateTransferCommandIfParametersAreInvalid() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new TransferCommand(0L, 0L, BigDecimal.valueOf(0)));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(1);
        assertThat(constraintViolationException.getMessage()).contains("amount: validation.transaction.transfer.amount.invalid");
    }
}
