package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.DepositGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.infra.domains.wallet.GetWalletByIdProvider;
import io.github.jtsato.walletservice.infra.domains.wallet.UpdateWalletByIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class DepositProvider implements DepositGateway {

    private final GetWalletByIdProvider getWalletByIdProvider;
    private final GetLocalDateTime getLocalDateTime;
    private final RegisterTransactionProvider registerTransactionProvider;
    private final UpdateWalletByIdProvider updateWalletByIdProvider;

    @Override
    public Wallet execute(final Wallet wallet, final Transaction transaction) {

        final Wallet currentWallet = getWalletByIdProvider.execute(wallet.id()).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(wallet.id())));
        final Transaction transactionUpdated = registerTransactionProvider.execute(transaction);
        final Wallet walletUpdated = currentWallet.withBalance(currentWallet.balance().add(transactionUpdated.amount())).withUpdatedAt(getLocalDateTime.now());
        return updateWalletByIdProvider.execute(walletUpdated).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(walletUpdated.id())));
    }
}
