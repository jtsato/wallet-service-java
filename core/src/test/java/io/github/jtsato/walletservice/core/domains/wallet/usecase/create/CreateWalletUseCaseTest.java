package io.github.jtsato.walletservice.core.domains.wallet.usecase.create;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Create Wallet Use Case Test")
class CreateWalletUseCaseTest {

    @Mock private final GetLocalDateTime getLocalDateTime = Mockito.mock(GetLocalDateTime.class);
    @Mock private final RegisterWalletGateway gateway = Mockito.mock(RegisterWalletGateway.class);

    @InjectMocks
    private final CreateWalletUseCase useCase = new CreateWalletUseCaseImpl(getLocalDateTime, gateway);

    @DisplayName("Successfully to create wallet")
    @Test
    void successfullyToCreateWallet() {

        // Arrange
        when(getLocalDateTime.now())
                .thenReturn(LocalDateTime.parse("2020-03-12T22:04:59.123"));

        final CreateWalletCommand command = new CreateWalletCommand("1001");

        when(gateway.execute(buildWalletToBeRegistered()))
                .thenReturn(buildRegisteredWallet());

        // Act
        final Wallet wallet = useCase.execute(command);

        // Assert
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.userId()).isEqualTo("1001");
        assertThat(wallet.balance()).isEqualTo(BigDecimal.ZERO);
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    private static Wallet buildWalletToBeRegistered() {
        return new Wallet(null, "1001", BigDecimal.ZERO, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    private static Wallet buildRegisteredWallet() {
        return new Wallet(1L, "1001", BigDecimal.ZERO, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }
}
