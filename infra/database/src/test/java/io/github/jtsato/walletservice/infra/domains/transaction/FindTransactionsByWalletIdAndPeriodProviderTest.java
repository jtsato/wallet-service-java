package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
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

@DisplayName("Find Transactions By Wallet Id And Period Provider Test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(FindTransactionsByWalletIdAndPeriodProviderTest.class)
@Sql("FindTransactionsByWalletIdAndPeriodProviderTest.sql")
class FindTransactionsByWalletIdAndPeriodProviderTest {

    @Autowired
    private FindTransactionsByWalletIdAndPeriodProvider findTransactionsByWalletIdAndPeriodProvider;

    @Autowired
    private TransactionRepository transactionRepository;

    @DisplayName("Successful to find transactions by wallet id and period test")
    @Test
    void successfulToFindTransactionsByWalletIdAndPeriodTest() {
        // Arrange
        // Act
        final Page<Transaction> transactions = findTransactionsByWalletIdAndPeriodProvider.execute(1L, LocalDateTime.parse("2025-02-12T22:04:59.123"), LocalDateTime.parse("2025-02-12T22:04:59.456"), 0, 10, "createdAt");

        // Assert
        assertThat(transactions).isNotNull();

        assertThat(transactions.getPageable().getPage()).isZero();
        assertThat(transactions.getPageable().getSize()).isEqualTo(10);
        assertThat(transactions.getPageable().getSize()).isEqualTo(10);
        assertThat(transactions.getPageable().getTotalPages()).isOne();
        assertThat(transactions.getPageable().getNumberOfElements()).isOne();
        assertThat(transactions.getPageable().getTotalOfElements()).isOne();

        assertThat(transactions.getContent()).isNotEmpty();
        assertThat(transactions.getContent().size()).isOne();

        final Transaction transaction = transactions.getContent().getFirst();
        assertThat(transaction.id()).isEqualTo(1L);
        assertThat(transaction.wallet()).isNotNull();
        assertThat(transaction.amount()).isEqualByComparingTo(BigDecimal.valueOf(1001.01));
        assertThat(transaction.type()).isEqualTo(Type.DEPOSIT);
        assertThat(transaction.createdAt()).isEqualTo(LocalDateTime.parse("2025-02-12T22:04:59.123"));

        assertThat(transactionRepository.count()).isEqualTo(1L);
    }
}
