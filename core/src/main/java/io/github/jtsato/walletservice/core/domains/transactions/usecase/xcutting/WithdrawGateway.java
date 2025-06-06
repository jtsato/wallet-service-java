package io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

public interface WithdrawGateway {

    Wallet execute(final Wallet wallet, final Transaction transaction);
}
