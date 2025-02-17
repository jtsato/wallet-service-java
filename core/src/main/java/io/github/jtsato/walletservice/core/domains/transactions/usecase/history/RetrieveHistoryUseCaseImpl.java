package io.github.jtsato.walletservice.core.domains.transactions.usecase.history;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.FindTransactionsByWalletIdAndPeriodGateway;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jorge Takeshi Sato
 */

@Named
@RequiredArgsConstructor
public class RetrieveHistoryUseCaseImpl implements RetrieveHistoryUseCase {

    private final FindTransactionsByWalletIdAndPeriodGateway gateway;

    @Override
    public Page<Transaction> execute(final Long walletId, final String initialDate, final String finalDate, final Integer pageNumber, final Integer pageSize, final String orderBy) {

        final LocalDateTime initialDateParsed = LocalDateTime.parse(initialDate, DateTimeFormatter.ISO_DATE_TIME);
        final LocalDateTime finalDateParsed = LocalDateTime.parse(finalDate, DateTimeFormatter.ISO_DATE_TIME);

        return gateway.execute(walletId, initialDateParsed, finalDateParsed, pageNumber, pageSize, orderBy);
    }
}
