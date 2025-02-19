package io.github.jtsato.walletservice.core.domains.transaction.usecase.history;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.common.paging.Pageable;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryUseCase;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryUseCaseImpl;
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
class RetrieveHistoryUseCaseTest {

    @Mock
    private final FindTransactionsByWalletIdAndPeriodGateway gateway = Mockito.mock(FindTransactionsByWalletIdAndPeriodGateway.class);

    @InjectMocks
    private final RetrieveHistoryUseCase useCase = new RetrieveHistoryUseCaseImpl(gateway);

    @DisplayName("Successful to retrieve history by wallet id when there is no transaction")
    @Test
    void successfulToRetrieveHistoryWhenThereIsNoTransaction() {
        // Arrange
        when(gateway.execute(1L, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"), 0, 1, "createdAt:asc"))
                .thenReturn(new Page<Transaction>() {
                    @Override
                    public List<Transaction> getContent() {
                        return Collections.emptyList();
                    }

                    @Override
                    public Pageable getPageable() {
                        return new Pageable(0, 1, 0, 0L, 0);
                    }
                });

        // Act
        final Page<Transaction> pageOfTransactions = useCase.execute(1L,"2020-03-12T22:04:59.123","2020-03-12T22:04:59.456", 0, 1, "createdAt:asc");

        // Assert
        assertThat(pageOfTransactions).isNotNull();

        final Pageable pageable = pageOfTransactions.getPageable();

        assertThat(pageable).isNotNull();
        assertThat(pageable.getPage()).isZero();
        assertThat(pageable.getSize()).isOne();
        assertThat(pageable.getNumberOfElements()).isZero();
        assertThat(pageable.getTotalOfElements()).isZero();
        assertThat(pageable.getTotalPages()).isZero();
        assertThat(pageOfTransactions.getContent()).isNotNull().isEmpty();
    }

    @DisplayName("Successful to retrieve history by wallet id when there is transactions")
    @Test
    void successfulToRetrieveHistoryWhenThereIsTransactions() {
        // Arrange
        when(gateway.execute(1L, LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"), 0, 1, "createdAt:asc"))
                .thenReturn(mockFindTransactionsByWalletIdAndPeriodGatewayOut());

        // Act
        final Page<Transaction> pageOfTransactions = new Page<>() {
            @Override
            public List<Transaction> getContent() {
                return Collections.singletonList(new Transaction(1L, new Wallet(1L, "purple", new BigDecimal("100.01"), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456")), new BigDecimal("100.01"), Type.DEPOSIT, null, LocalDateTime.parse("2020-04-12T22:04:59.123")));
            }

            @Override
            public Pageable getPageable() {
                return new Pageable(0, 1, 1, 1L, 1);
            }
        };

        // Assert
        assertThat(pageOfTransactions).isNotNull();

        final Pageable pageable = pageOfTransactions.getPageable();

        assertThat(pageable).isNotNull();
        assertThat(pageable.getPage()).isZero();
        assertThat(pageable.getSize()).isEqualTo(1);
        assertThat(pageable.getNumberOfElements()).isEqualTo(1);
        assertThat(pageable.getTotalOfElements()).isEqualTo(1);
        assertThat(pageable.getTotalPages()).isEqualTo(1);

        assertThat(pageOfTransactions.getContent()).isNotNull().isNotEmpty().size().isEqualTo(1);

        final Transaction transaction = pageOfTransactions.getContent().getFirst();

        assertThat(transaction.id()).isEqualTo(1L);
        assertThat(transaction.wallet().id()).isEqualTo(1L);
        assertThat(transaction.wallet().userId()).isEqualTo("purple");
        assertThat(transaction.wallet().balance()).isEqualTo(new BigDecimal("100.01"));
        assertThat(transaction.wallet().createdAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        assertThat(transaction.wallet().updatedAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.456"));
        assertThat(transaction.amount()).isEqualTo(new BigDecimal("100.01"));
        assertThat(transaction.type()).isEqualTo(Type.DEPOSIT);
        assertThat(transaction.destinationWallet()).isNull();
        assertThat(transaction.createdAt()).isEqualTo(LocalDateTime.parse("2020-04-12T22:04:59.123"));
    }

    private Page<Transaction> mockFindTransactionsByWalletIdAndPeriodGatewayOut() {
        Wallet wallet = new Wallet(1L, "purple", new BigDecimal("100.01"), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.456"));
        return new Page<>() {
            @Override
            public List<Transaction> getContent() {
                return Collections.singletonList(new Transaction(1L, wallet, new BigDecimal("100.01"), Type.DEPOSIT, null, LocalDateTime.parse("2020-04-12T22:04:59.123")));
            }

            @Override
            public Pageable getPageable() {
                return new Pageable(0, 1, 1, 1L, 1);
            }
        };
    }
}
