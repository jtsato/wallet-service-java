package io.github.jtsato.walletservice.core.domains.transaction.usecase.history;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryCommand;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Retrieve History Command Test")
class RetrieveHistoryCommandTest {

    @DisplayName("Fail to create RetrieveHistoryCommand if parameters are null")
    @Test
    void failToCreateRetrieveHistoryCommandIfParametersAreNull() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new RetrieveHistoryCommand(null, null, null));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(2);
        assertThat(constraintViolationException.getMessage()).contains("walletId: validation.wallet.id.null");
        assertThat(constraintViolationException.getMessage()).contains("initialDate: validation.transaction.history.initial.date.blank");
    }

    @DisplayName("Fail to create RetrieveHistoryCommand if parameters are invalid")
    @Test
    void failToCreateRetrieveHistoryCommandIfParametersAreInvalid() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new RetrieveHistoryCommand(0L, "2020-02-30T22:04:59.123", "2020-02-30T22:04:59.456"));

        // Assert
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(2);
        assertThat(constraintViolationException.getMessage()).contains("initialDate: validation.transaction.history.initial.date.invalid");
        assertThat(constraintViolationException.getMessage()).contains( "finalDate: validation.transaction.history.final.date.invalid");
    }


    @DisplayName("Successful to create RetrieveHistoryCommand")
    @Test
    void successfulToCreateRetrieveHistoryCommand() {

        // Arrange
        // Act
        final RetrieveHistoryCommand retrieveHistoryCommand = new RetrieveHistoryCommand(1L, "2020-03-12T22:04:59.123", "2020-03-12T22:04:59.456");

        // Assert
        assertThat(retrieveHistoryCommand).isNotNull();
        assertThat(retrieveHistoryCommand.getWalletId()).isEqualTo(1L);
        assertThat(retrieveHistoryCommand.getInitialDate()).isEqualTo("2020-03-12T22:04:59.123");
        assertThat(retrieveHistoryCommand.getFinalDate()).isEqualTo("2020-03-12T22:04:59.456");
    }
}
