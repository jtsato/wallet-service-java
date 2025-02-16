package io.github.jtsato.walletservice.core.domains.wallet.usecase.history;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.FindTransactionsByWalletIdAndPeriodGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("History Use Case Test")
class HistoryUseCaseTest {

    @Mock
    private final FindTransactionsByWalletIdAndPeriodGateway gateway = Mockito.mock(FindTransactionsByWalletIdAndPeriodGateway.class);

    @InjectMocks
    private final HistoryUseCase useCase = new HistoryUseCaseImpl(gateway);

    @DisplayName("Successful to retrieve history by wallet id when there is no transaction")
    @Test
    void successfulToRetrieveHistoryWhenThereIsNoTransaction() {
        // Arrange
        when(gateway.execute(1L, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"))).thenReturn(Collections.emptyList());

        // Act
        final List<Transaction> transactions = useCase.execute(1L, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"));

        // Assert
        assertThat(transactions).isNotNull().isEmpty();
    }

    @DisplayName("Successful to retrieve history by wallet id when there is transactions")
    @Test
    void successfulToRetrieveHistoryWhenThereIsTransactions() {
        // Arrange
        when(gateway.execute(1L, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"))).thenReturn(mockFindTransactionsByWalletIdAndPeriodGatewayOut());

        // Act
        final List<Transaction> transactions = useCase.execute(1L, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"));

        // Assert
        assertThat(transactions).isNotNull().isNotEmpty().size().isEqualTo(1);

        final Transaction transaction = transactions.getFirst();

        assertThat(transaction.id()).isEqualTo(1L);
        assertThat(transaction.wallet().id()).isEqualTo(1L);
        assertThat(transaction.wallet().userId()).isEqualTo("purple");
        assertThat(transaction.wallet().balance()).isEqualTo(new BigDecimal("100.01"));
        assertThat(transaction.wallet().createdAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        assertThat(transaction.wallet().updatedAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.456"));
        assertThat(transaction.amount()).isEqualTo(new BigDecimal("100.01"));
        assertThat(transaction.type()).isEqualTo(Type.DEPOSIT);
        assertThat(transaction.createdAt()).isEqualTo(LocalDateTime.parse("2020-04-12T22:04:59.123"));
    }

    private List<Transaction> mockFindTransactionsByWalletIdAndPeriodGatewayOut() {
        Wallet wallet = new Wallet(1L, "purple", new BigDecimal("100.01"), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"));
        return Collections.singletonList(new Transaction(1L, wallet, new BigDecimal("100.01"), Type.DEPOSIT, LocalDateTime.parse("2020-04-12T22:04:59.123")));
    }
}
