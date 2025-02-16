package io.github.jtsato.walletservice.core.domains.wallet.usecase.balance;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

@FunctionalInterface
public interface RetrieveBalanceUseCase {

    Wallet execute(final Long walletId);
}
