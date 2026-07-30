package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.WithdrawGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.core.exception.InsufficientBalanceException;
import io.github.jtsato.walletservice.infra.domains.wallet.UpdateWalletByIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class WithdrawProvider implements WithdrawGateway {

    private final GetLocalDateTime getLocalDateTime;
    private final RegisterTransactionProvider registerTransactionProvider;
    private final UpdateWalletByIdProvider updateWalletByIdProvider;

    @Override
    public Wallet execute(final Wallet currentWallet, final Transaction transaction) {

        final Wallet lockedWallet = updateWalletByIdProvider.findWithLockById(currentWallet.id())
                .orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(currentWallet.id())));
        if (lockedWallet.balance().compareTo(transaction.amount()) < 0) {
            throw new InsufficientBalanceException(lockedWallet.id());
        }
        final Wallet wallet = lockedWallet.withBalance(lockedWallet.balance().subtract(transaction.amount())).withUpdatedAt(getLocalDateTime.now());
        registerTransactionProvider.execute(new Transaction(
                transaction.id(), lockedWallet, transaction.amount(), transaction.type(), transaction.destinationWallet(), transaction.createdAt()));

        return updateWalletByIdProvider.execute(wallet).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(wallet.id())));
    }
}
