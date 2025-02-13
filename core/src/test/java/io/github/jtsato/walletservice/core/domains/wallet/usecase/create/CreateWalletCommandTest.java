package io.github.jtsato.walletservice.core.domains.wallet.usecase.create;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Create Wallet Command Test")
class CreateWalletCommandTest {

    @DisplayName("Fail to create CreateWalletCommand with null userId")
    @Test
    void failToCreateCreateWalletCommandWithNullUserId() {

        // Arrange
        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> new CreateWalletCommand(null));

        // Assert

        assertThat(exception).isInstanceOf(ConstraintViolationException.class);

        final ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
        assertThat(constraintViolationException.getConstraintViolations()).hasSize(1);
        assertThat(exception.getMessage()).isEqualTo("userId: validation.wallet.userId.blank");
    }

    @DisplayName("Successfully to create CreateWalletCommand")
    @Test
    void successfullyToCreateCreateWalletCommand() {

        // Arrange
        // Act
        final CreateWalletCommand command = new CreateWalletCommand("1001");

        // Assert
        assertThat(command).isNotNull();
        assertThat(command.getUserId()).isEqualTo("1001");
    }
}
