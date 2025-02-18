package io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

@FunctionalInterface
public interface DepositUseCase {

    Wallet execute(final DepositCommand command);
}
