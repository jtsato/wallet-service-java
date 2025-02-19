package io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

public interface TransferGateway {

    Wallet execute(final Wallet originWallet, final Wallet destinationWallet, final Transaction transaction);
}
