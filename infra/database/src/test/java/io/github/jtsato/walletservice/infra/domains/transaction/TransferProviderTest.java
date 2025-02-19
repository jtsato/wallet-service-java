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

@DisplayName("Transfer Provider Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import({RegisterTransactionProvider.class, GetLocalDateTimeMockImpl.class, UpdateWalletByIdProvider.class, GetWalletByIdProvider.class, TransferProvider.class})
@Sql("TransferProviderTest.sql")
class TransferProviderTest {

    @Autowired
    private GetWalletByIdProvider getWalletByIdProvider;

    @Autowired
    private TransferProvider transferProvider;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @DisplayName("Successful to transfer if parameters are valid")
    @Test
    void successfulToTransferIfParametersAreValid() {

        // Arrange
        final Wallet originWallet = getWalletByIdProvider.execute(1L).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", "1"));
        final Wallet destinationWallet = getWalletByIdProvider.execute(2L).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", "2"));

        final Transaction transaction = new Transaction(null, originWallet, BigDecimal.valueOf(150L), Type.TRANSFER, destinationWallet, LocalDateTime.parse("2025-02-12T22:04:59.123"));

        // Act
        final Wallet wallet1 = transferProvider.execute(originWallet, destinationWallet, transaction);
        final Wallet wallet2 = getWalletByIdProvider.execute(2L).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", "2"));

        // Assert
        assertThat(wallet1).isNotNull();
        assertThat(wallet1.id()).isNotNull();
        assertThat(wallet1.userId()).isEqualTo("red");
        assertThat(wallet1.balance()).isEqualByComparingTo(BigDecimal.valueOf(850));
        assertThat(wallet1.createdAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(wallet1.updatedAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(walletRepository.count()).isEqualTo(2L);
        assertThat(transactionRepository.count()).isEqualTo(1L);

        assertThat(wallet2).isNotNull();
        assertThat(wallet2.id()).isNotNull();
        assertThat(wallet2.userId()).isEqualTo("blue");
        assertThat(wallet2.balance()).isEqualByComparingTo(BigDecimal.valueOf(650));
        assertThat(wallet2.createdAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(wallet2.updatedAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));
        assertThat(walletRepository.count()).isEqualTo(2L);
        assertThat(transactionRepository.count()).isEqualTo(1L);
    }
}
