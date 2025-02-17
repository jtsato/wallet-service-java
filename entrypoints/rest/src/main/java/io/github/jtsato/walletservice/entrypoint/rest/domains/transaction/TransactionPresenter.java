package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionPresenter {

    public static TransactionResponse of(final Transaction transaction) {
        return new TransactionResponse(transaction.id(),
                                       transaction.amount(),
                                       transaction.type().name(),
                                       transaction.createdAt());
    }
}
