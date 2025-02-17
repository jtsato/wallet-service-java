package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.history;

import java.util.ArrayList;
import java.util.List;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.TransactionPresenter;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.TransactionResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Jorge Takeshi Sato
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveHistoryPresenter {

    public static RetrieveHistoryWrapperResponse of(final Page<Transaction> page) {
        final List<Transaction> transactions = page.getContent();
        final List<TransactionResponse> content = new ArrayList<>(transactions.size());
        transactions.forEach(transaction -> content.add(TransactionPresenter.of(transaction)));
        return new RetrieveHistoryWrapperResponse(content, page.getPageable());
    }

    public static List<TransactionResponse> of(final List<TransactionResponse> content) {
        return new ArrayList<>(content);
    }
}
