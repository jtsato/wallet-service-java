package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.history;

import io.github.jtsato.walletservice.core.common.paging.PageImpl;
import io.github.jtsato.walletservice.core.common.paging.Pageable;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.TransactionResponse;
import java.util.List;

public class RetrieveHistoryWrapperResponse extends PageImpl<TransactionResponse> {

    public RetrieveHistoryWrapperResponse(final List<TransactionResponse> content, final Pageable pageable) {
        super(content, pageable);
    }
}
