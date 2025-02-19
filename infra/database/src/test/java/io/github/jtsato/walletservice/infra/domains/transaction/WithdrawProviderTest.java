package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.infra.common.GetLocalDateTimeMockImpl;
import io.github.jtsato.walletservice.infra.domains.wallet.GetWalletByIdProvider;
import io.github.jtsato.walletservice.infra.domains.wallet.UpdateWalletByIdProvider;
import io.github.jtsato.walletservice.infra.domains.wallet.WalletRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Withdraw Provider Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import({RegisterTransactionProvider.class, GetLocalDateTimeMockImpl.class, UpdateWalletByIdProvider.class, GetWalletByIdProvider.class, WithdrawProvider.class})
@Sql("WithdrawProviderTest.sql")
class WithdrawProviderTest {

    @Autowired
    private GetWalletByIdProvider getWalletByIdProvider;

    @Autowired
    private WithdrawProvider withdrawProvider;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @DisplayName("Successful to withdraw if parameters are valid")
    @Test
    void successfulToWithdrawIfParametersAreValid() {

        // Arrange
        final Wallet currentWallet = getWalletByIdProvider.execute(1L).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", "1"));
        final Transaction transaction = new Transaction(null, currentWallet, BigDecimal.valueOf(100L), Type.WITHDRAWAL, null, LocalDateTime.parse("2025-02-12T22:04:59.123"));

        // Act
        final Wallet wallet = withdrawProvider.execute(currentWallet, transaction);

        // Assert
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isNotNull();
        assertThat(wallet.userId()).isEqualTo("red");
        assertThat(wallet.balance()).isEqualByComparingTo(BigDecimal.valueOf(900));
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(walletRepository.count()).isEqualTo(1L);
        assertThat(transactionRepository.count()).isEqualTo(1L);
    }
}
