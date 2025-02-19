package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Register Wallet Provider Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(RegisterWalletProvider.class)
class RegisterWalletProviderTest {

    @Autowired
    private RegisterWalletProvider registerWalletProvider;

    @Autowired
    private WalletRepository walletRepository;

    @DisplayName("Successful to register wallet if parameters are valid")
    @Test
    void successfulToRegisterWalletIfParametersAreValid() {
        // Arrange
        final Wallet newWallet = new Wallet(null, "red", BigDecimal.ZERO, LocalDateTime.parse("2025-02-12T22:04:59.123"), LocalDateTime.parse("2025-02-12T22:04:59.123"));

        // Act
        final Wallet wallet = registerWalletProvider.execute(newWallet);

        // Assert
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isNotNull();
        assertThat(wallet.userId()).isEqualTo("red");
        assertThat(wallet.balance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(walletRepository.count()).isEqualTo(1L);
    }

    @DisplayName("Fail to register wallet if parameters are invalid")
    @Test
    void failToRegisterWalletIfParametersAreInvalid() {
        // Arrange
        final Wallet newWallet = new Wallet(null, null, null, null, null);

        // Act
        final Exception exception = Assertions.assertThrows(Exception.class, () -> registerWalletProvider.execute(newWallet));

        // Assert
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
    }
}
