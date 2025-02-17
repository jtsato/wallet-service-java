package io.github.jtsato.walletservice.core.domains.transactions.usecase.history;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;

/**
 * @author Jorge Takeshi Sato
 */

public interface RetrieveHistoryUseCase {

    Page<Transaction> execute(final Long walletId, final String startDate, final String endDate, final Integer pageNumber, final Integer pageSize, final String orderBy);
}
