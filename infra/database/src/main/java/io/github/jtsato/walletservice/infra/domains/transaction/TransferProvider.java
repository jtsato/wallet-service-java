package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.TransferGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.infra.domains.wallet.UpdateWalletByIdProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TransferProvider implements TransferGateway {

    private final RegisterTransactionProvider registerTransactionProvider;
    private final GetLocalDateTime getLocalDateTime;
    private final UpdateWalletByIdProvider updateWalletByIdProvider;

    @Override
    public Wallet execute(final Wallet originWallet, final Wallet destinationWallet, final Transaction transaction) {

        final Transaction transactionUpdated = registerTransactionProvider.execute(transaction);

        final Wallet source = originWallet.withBalance(originWallet.balance().subtract(transactionUpdated.amount())).withUpdatedAt(getLocalDateTime.now());
        final Wallet destination = destinationWallet.withBalance(destinationWallet.balance().add(transactionUpdated.amount())).withUpdatedAt(getLocalDateTime.now());

        updateWalletByIdProvider.execute(destination).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(destinationWallet.id())));

        return updateWalletByIdProvider.execute(source).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(originWallet.id())));
    }
}
