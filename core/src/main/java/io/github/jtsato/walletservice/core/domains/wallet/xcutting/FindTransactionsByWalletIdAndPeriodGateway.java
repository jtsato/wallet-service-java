package io.github.jtsato.walletservice.core.domains.wallet.xcutting;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@FunctionalInterface
public interface FindTransactionsByWalletIdAndPeriodGateway {

    List<Transaction> execute(final Long walletId, final LocalDateTime initialDate, final LocalDateTime finalDate);
}
