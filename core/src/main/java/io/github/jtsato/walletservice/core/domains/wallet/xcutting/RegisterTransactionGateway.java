package io.github.jtsato.walletservice.core.domains.wallet.xcutting;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;

@FunctionalInterface
public interface RegisterTransactionGateway {

    Transaction execute(final Transaction transaction);
}
