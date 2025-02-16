package io.github.jtsato.walletservice.core.domains.wallet.usecase.balance;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

@FunctionalInterface
public interface GetBalanceByWalletIdUseCase {

    Wallet execute(final Long walletId);
}
