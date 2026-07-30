package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.DepositGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.infra.domains.wallet.UpdateWalletByIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class DepositProvider implements DepositGateway {

    private final RegisterTransactionProvider registerTransactionProvider;
    private final GetLocalDateTime getLocalDateTime;
    private final UpdateWalletByIdProvider updateWalletByIdProvider;

    @Override
    public Wallet execute(final Wallet currentWallet, final Transaction transaction) {

        final Wallet lockedWallet = updateWalletByIdProvider.findWithLockById(currentWallet.id())
                .orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(currentWallet.id())));
        final Wallet wallet = lockedWallet.withBalance(lockedWallet.balance().add(transaction.amount())).withUpdatedAt(getLocalDateTime.now());
        registerTransactionProvider.execute(new Transaction(
                transaction.id(), lockedWallet, transaction.amount(), transaction.type(), transaction.destinationWallet(), transaction.createdAt()));

        return updateWalletByIdProvider.execute(wallet).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(wallet.id())));
    }
}
