package io.github.jtsato.walletservice.core.domains.wallet.xcutting;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

import java.util.Optional;

@FunctionalInterface
public interface UpdateWalletByIdGateway {

    Optional<Wallet> execute(final Wallet wallet);
}
