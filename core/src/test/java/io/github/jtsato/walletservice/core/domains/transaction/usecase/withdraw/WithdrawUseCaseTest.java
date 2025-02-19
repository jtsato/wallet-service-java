package io.github.jtsato.walletservice.core.domains.transaction.usecase.withdraw;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawUseCase;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawUseCaseImpl;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.WithdrawGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Withdraw Use Case Test")
class WithdrawUseCaseTest {

    @Mock private final GetWalletByIdGateway getWalletByIdGateway = Mockito.mock(GetWalletByIdGateway.class);
    @Mock private final GetLocalDateTime getLocalDateTime = Mockito.mock(GetLocalDateTime.class);
    @Mock private final WithdrawGateway withdrawGateway = Mockito.mock(WithdrawGateway.class);

    @InjectMocks
    private final WithdrawUseCase useCase = new WithdrawUseCaseImpl(getWalletByIdGateway, getLocalDateTime, withdrawGateway);

    @DisplayName("Successfully to withdraw")
    @Test
    void successfullyToWithdraw() {

        // Arrange
        when(getWalletByIdGateway.execute(1L)).thenReturn(Optional.of(buildCurrentWallet()));
        when(getLocalDateTime.now()).thenReturn(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        when(withdrawGateway.execute(buildCurrentWallet(), buildTransaction())).thenReturn(buildUpdatedWallet());

        final WithdrawCommand command = new WithdrawCommand(1L, "10");

        // Act
        final Wallet wallet = useCase.execute(command);

        // Assert
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.userId()).isEqualTo("1001");
        assertThat(wallet.balance()).isEqualTo(new BigDecimal(90));
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    @DisplayName("Fail to withdraw if wallet not found")
    @Test
    void failToWithdrawIfWalletNotFound() {

        // Arrange
        when(getWalletByIdGateway.execute(1L)).thenReturn(Optional.empty());

        final WithdrawCommand command = new WithdrawCommand(1L, "10");

        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> useCase.execute(command));

        // Assert
        assertThat(exception).isInstanceOf(NotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("validation.wallet.id.notfound");
    }

    @DisplayName("Fail to withdraw if wallet has insufficient balance")
    @Test
    void failToWithdrawIfWalletHasInsufficientBalance() {

        // Arrange
        when(getWalletByIdGateway.execute(1L)).thenReturn(Optional.of(buildCurrentWallet()));

        final WithdrawCommand command = new WithdrawCommand(1L, "110");

        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> useCase.execute(command));

        // Assert
        assertThat(exception).isInstanceOf(Exception.class);
        assertThat(exception.getMessage()).isEqualTo("validation.wallet.insufficient.balance");
    }

    private Wallet buildCurrentWallet() {
        return new Wallet(1L, "1001", new BigDecimal(100), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    private Transaction buildTransaction() {
        return new Transaction(null, buildCurrentWallet(), BigDecimal.TEN, Type.WITHDRAWAL, null, LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }
    
    private Wallet buildUpdatedWallet() {
        return new Wallet(1L, "1001", new BigDecimal(90), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }
}
