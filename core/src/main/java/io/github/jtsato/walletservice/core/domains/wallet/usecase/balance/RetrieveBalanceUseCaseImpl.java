package io.github.jtsato.walletservice.core.domains.wallet.usecase.balance;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Named
@RequiredArgsConstructor
public class RetrieveBalanceUseCaseImpl implements RetrieveBalanceUseCase {

    private final GetWalletByIdGateway getWalletByIdGateway;

    @Override
    public Wallet execute(final Long walletId) {

        if (walletId == null) {
            throw new IllegalArgumentException("validation.wallet.id.null");
        }

        final Optional<Wallet> optional = getWalletByIdGateway.execute(walletId);

        return optional.orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(walletId)));
    }
}
