package io.github.jtsato.walletservice.core.domains.wallet.usecase.balance;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface RetrieveHistoricalBalanceUseCase {

    List<Transaction> execute(final Long walletId, final LocalDateTime initialDate, final LocalDateTime finalDate);
}
