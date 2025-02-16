package io.github.jtsato.walletservice.core.domains.wallet.usecase.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

@DisplayName("Get Balance By Wallet Id Use Case Test")
class GetBalanceByWalletIdUseCaseTest {

    @Mock
    private final GetWalletByIdGateway gateway = Mockito.mock(GetWalletByIdGateway.class);

    @InjectMocks
    private final GetBalanceByWalletIdUseCase useCase = new GetBalanceByWalletIdUseCaseImpl(gateway);

    @DisplayName("Fail to get balance by wallet id when wallet id is null")
    @Test
    void failToGetBalanceByWalletIdWhenWalletIdIsNull() {
        // Arrange
        when(gateway.execute(null)).thenReturn(Optional.empty());

        // Act
        // Assert
        final IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("validation.wallet.id.null");
    }

    @DisplayName("Successful to get balance by wallet id")
    @Test
    void successfulToGetBalanceByWalletId() {
        // Arrange
        when(gateway.execute(1L)).thenReturn(Optional.of(mockGetWalletByIdGatewayOut()));

        // Act
        final Wallet wallet = useCase.execute(1L);

        // Assert
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.userId()).isEqualTo("yellow");
        assertThat(wallet.balance()).isEqualTo(new BigDecimal("100.01"));
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.456"));
    }

    private Wallet mockGetWalletByIdGatewayOut() {
        return new Wallet(1L, "yellow", new BigDecimal("100.01"), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"));
    }
}
