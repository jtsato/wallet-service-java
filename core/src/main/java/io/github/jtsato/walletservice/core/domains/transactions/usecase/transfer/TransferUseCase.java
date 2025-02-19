package io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

@FunctionalInterface
public interface TransferUseCase {

    Wallet execute(final TransferCommand command);
}
