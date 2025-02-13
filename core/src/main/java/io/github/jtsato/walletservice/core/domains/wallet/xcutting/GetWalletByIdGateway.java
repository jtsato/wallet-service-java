package io.github.jtsato.walletservice.core.domains.wallet.xcutting;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

import java.util.Optional;

@FunctionalInterface
public interface GetWalletByIdGateway {

    Optional<Wallet> execute(final Long id);
}
