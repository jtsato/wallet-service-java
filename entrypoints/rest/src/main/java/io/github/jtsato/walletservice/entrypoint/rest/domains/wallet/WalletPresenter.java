package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WalletPresenter {

    public static WalletResponse of(Wallet wallet) {
        return new WalletResponse(wallet.id(),
                                  wallet.userId(),
                                  wallet.balance(),
                                  wallet.createdAt(),
                                  wallet.updatedAt());
    }
}
