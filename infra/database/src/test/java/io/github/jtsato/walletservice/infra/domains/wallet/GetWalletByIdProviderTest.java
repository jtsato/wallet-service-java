package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Get Wallet by Id Provider Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(GetWalletByIdProvider.class)
@Sql("GetWalletByIdProviderTest.sql")
class GetWalletByIdProviderTest {

    @Autowired
    private GetWalletByIdProvider getWalletByIdProvider;

    @Autowired
    private WalletRepository walletRepository;

    @DisplayName("Successful to get wallet by id if found")
    @Test
    void successfulToGetWalletByIdIfFound() {
        // Arrange
        // Act
        final Optional<Wallet> optional = getWalletByIdProvider.execute(1L);

        // Assert
        assertThat(optional).isPresent();

        final Wallet wallet = optional.get();

        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.userId()).isEqualTo("yellow");
        assertThat(wallet.balance()).isEqualByComparingTo(BigDecimal.valueOf(2002.02));
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.456"));
        assertThat(walletRepository.count()).isEqualTo(1L);
    }

    @DisplayName("Fail to get wallet by id if not found")
    @Test
    void failToGetWalletByIdIfNotFound() {
        // Arrange
        // Act
        final Optional<Wallet> optional = getWalletByIdProvider.execute(999L);

        // Assert
        assertThat(optional).isNotPresent();
        assertThat(walletRepository.count()).isEqualTo(1L);
    }
}
