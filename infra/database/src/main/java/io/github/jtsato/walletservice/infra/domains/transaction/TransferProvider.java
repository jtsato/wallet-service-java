package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.TransferGateway;
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
public class TransferProvider implements TransferGateway {

    private static final String WALLET_NOT_FOUND = "validation.wallet.id.notfound";

    private final RegisterTransactionProvider registerTransactionProvider;
    private final GetLocalDateTime getLocalDateTime;
    private final UpdateWalletByIdProvider updateWalletByIdProvider;

    @Override
    public Wallet execute(final Wallet originWallet, final Wallet destinationWallet, final Transaction transaction) {

        final boolean originFirst = originWallet.id() < destinationWallet.id();
        final Wallet firstLocked = updateWalletByIdProvider.findWithLockById(originFirst ? originWallet.id() : destinationWallet.id())
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND, String.valueOf(originFirst ? originWallet.id() : destinationWallet.id())));
        final Wallet secondLocked = updateWalletByIdProvider.findWithLockById(originFirst ? destinationWallet.id() : originWallet.id())
                .orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND, String.valueOf(originFirst ? destinationWallet.id() : originWallet.id())));
        final Wallet lockedOrigin = originFirst ? firstLocked : secondLocked;
        final Wallet lockedDestination = originFirst ? secondLocked : firstLocked;
        if (lockedOrigin.balance().compareTo(transaction.amount()) < 0) {
            throw new InsufficientBalanceException(lockedOrigin.id());
        }
        final Wallet source = lockedOrigin.withBalance(lockedOrigin.balance().subtract(transaction.amount())).withUpdatedAt(getLocalDateTime.now());
        final Wallet destination = lockedDestination.withBalance(lockedDestination.balance().add(transaction.amount())).withUpdatedAt(getLocalDateTime.now());
        registerTransactionProvider.execute(new Transaction(
                transaction.id(), lockedOrigin, transaction.amount(), transaction.type(), lockedDestination, transaction.createdAt()));

        updateWalletByIdProvider.execute(destination).orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND, String.valueOf(destinationWallet.id())));

        return updateWalletByIdProvider.execute(source).orElseThrow(() -> new NotFoundException(WALLET_NOT_FOUND, String.valueOf(originWallet.id())));
    }
}
