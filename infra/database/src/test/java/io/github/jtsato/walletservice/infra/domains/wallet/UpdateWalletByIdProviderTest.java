package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Update Wallet by Id Provider Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(UpdateWalletByIdProvider.class)
@Sql("UpdateWalletByIdProviderTest.sql")
class UpdateWalletByIdProviderTest {

    private static final WalletMapper walletMapper = Mappers.getMapper(WalletMapper.class);

    @Autowired
    private UpdateWalletByIdProvider updateWalletByIdProvider;

    @Autowired
    private WalletRepository walletRepository;

    @DisplayName("Successful to update wallet by id")
    @Test
    void successfulToUpdateWalletById() {
        // Arrange
        final Wallet currentWallet = getCurrentBlackWallet()
                .withBalance(new BigDecimal("2.02"))
                .withUpdatedAt(LocalDateTime.parse("2025-02-12T22:04:59.456"));

        // Act
        final Optional<Wallet> optional = updateWalletByIdProvider.execute(currentWallet);

        // Assert
        final Wallet wallet = optional.orElse(null);

        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(currentWallet.id());
        assertThat(wallet.userId()).isEqualTo("black");
        assertThat(wallet.balance()).isEqualTo(new BigDecimal("2.02"));
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2025-02-13T00:00:01.000"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.456"));
        assertThat(walletRepository.count()).isEqualTo(1L);
    }

    @DisplayName("Fail to update wallet by id if not found")
    @Test
    void failToUpdateWalletByIdIfNotFound() {
        // Arrange
        final Wallet currentWallet = new Wallet(999L, "black", new BigDecimal("2.02"), LocalDateTime.parse("2025-02-12T22:04:59.123"), LocalDateTime.parse("2025-02-12T22:04:59.123"));

        // Act
        final Optional<Wallet> optional = updateWalletByIdProvider.execute(currentWallet);

        // Assert
        assertThat(optional).isNotPresent();
        assertThat(walletRepository.count()).isEqualTo(1L);
    }

    private Wallet getCurrentBlackWallet() {
        Optional<Wallet> optional = walletRepository.findByUserId("black").map(walletMapper::of);
        assertThat(optional).isPresent();

        return optional.get();
    }
}
