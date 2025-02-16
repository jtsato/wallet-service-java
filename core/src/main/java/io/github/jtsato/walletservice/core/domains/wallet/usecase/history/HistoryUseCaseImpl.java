package io.github.jtsato.walletservice.core.domains.wallet.usecase.history;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.FindTransactionsByWalletIdAndPeriodGateway;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Named
@RequiredArgsConstructor
public class HistoryUseCaseImpl implements HistoryUseCase {

    private final FindTransactionsByWalletIdAndPeriodGateway gateway;

    @Override
    public List<Transaction> execute(final Long walletId, final LocalDateTime initialDate, final LocalDateTime finalDate) {
        return gateway.execute(walletId, initialDate, finalDate);
    }
}
