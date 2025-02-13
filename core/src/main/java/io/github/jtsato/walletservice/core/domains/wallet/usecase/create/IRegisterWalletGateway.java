package io.github.jtsato.walletservice.core.domains.wallet.usecase.create;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

@FunctionalInterface
public interface IRegisterWalletGateway {

    Wallet execute(final Wallet wallet);
}
