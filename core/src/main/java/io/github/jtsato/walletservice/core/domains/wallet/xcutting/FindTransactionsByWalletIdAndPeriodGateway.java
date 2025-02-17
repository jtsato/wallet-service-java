package io.github.jtsato.walletservice.core.domains.wallet.xcutting;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;

import java.time.LocalDateTime;

@FunctionalInterface
public interface FindTransactionsByWalletIdAndPeriodGateway {

    Page<Transaction> execute(final Long walletId, final LocalDateTime initialDate, final LocalDateTime finalDate, final Integer pageNumber, final Integer size, final String orderBy);
}
