package io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

@FunctionalInterface
public interface WithdrawUseCase {

    Wallet execute(final WithdrawCommand command);
}
