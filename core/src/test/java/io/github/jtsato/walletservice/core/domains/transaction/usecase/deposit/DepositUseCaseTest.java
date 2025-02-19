package io.github.jtsato.walletservice.core.domains.transaction.usecase.deposit;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositUseCase;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositUseCaseImpl;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.DepositGateway;
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

@DisplayName("Deposit Use Case Test")
class DepositUseCaseTest {

    @Mock private final GetWalletByIdGateway getWalletByIdGateway = Mockito.mock(GetWalletByIdGateway.class);
    @Mock private final GetLocalDateTime getLocalDateTime = Mockito.mock(GetLocalDateTime.class);
    @Mock private final DepositGateway depositGateway = Mockito.mock(DepositGateway.class);

    @InjectMocks
    private final DepositUseCase useCase = new DepositUseCaseImpl(getWalletByIdGateway, getLocalDateTime, depositGateway);

    @DisplayName("Successfully to deposit")
    @Test
    void successfullyToDeposit() {

        // Arrange
        when(getWalletByIdGateway.execute(1L)).thenReturn(Optional.of(buildCurrentWallet()));
        when(getLocalDateTime.now()).thenReturn(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        when(depositGateway.execute(buildCurrentWallet(), buildTransaction())).thenReturn(buildUpdatedWallet());

        final DepositCommand command = new DepositCommand(1L, "10");

        // Act
        final Wallet wallet = useCase.execute(command);

        // Assert
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.userId()).isEqualTo("1001");
        assertThat(wallet.balance()).isEqualTo("10");
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    @DisplayName("Fail to deposit if wallet not found")
    @Test
    void failToDepositIfWalletNotFound() {

        // Arrange
        when(getWalletByIdGateway.execute(1L)).thenReturn(Optional.empty());

        final DepositCommand command = new DepositCommand(1L, "10");

        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> useCase.execute(command));

        // Assert
        assertThat(exception).isInstanceOf(NotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("validation.wallet.id.notfound");
    }

    private static Wallet buildCurrentWallet() {
        return new Wallet(1L, "1001", BigDecimal.ZERO, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    private static Transaction buildTransaction() {
        return new Transaction(null, new Wallet(1L, "1001", BigDecimal.ZERO, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123")), BigDecimal.TEN, Type.DEPOSIT, null, LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    private static Wallet buildUpdatedWallet() {
        return new Wallet(1L, "1001", BigDecimal.TEN, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

}
