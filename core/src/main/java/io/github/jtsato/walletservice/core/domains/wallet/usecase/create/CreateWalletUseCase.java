package io.github.jtsato.walletservice.core.domains.wallet.usecase.create;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

@FunctionalInterface
public interface CreateWalletUseCase {

    Wallet execute(final CreateWalletCommand command);
}
