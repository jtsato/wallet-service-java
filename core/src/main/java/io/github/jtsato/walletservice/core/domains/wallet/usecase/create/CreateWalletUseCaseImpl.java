package io.github.jtsato.walletservice.core.domains.wallet.usecase.create;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * A Use Case follows these steps:
 * - Takes input
 * - Validates business rules
 * - Manipulates the model state
 * - Returns output
 */

@Named
@RequiredArgsConstructor
public class CreateWalletUseCaseImpl implements CreateWalletUseCase {

    private final GetLocalDateTime getLocalDateTime;
    private final RegisterWalletGateway registerWalletGateway;

    @Override
    public Wallet execute(final CreateWalletCommand command) {

        final LocalDateTime createdAt = getLocalDateTime.now();
        final LocalDateTime updatedAt = getLocalDateTime.now();
        final Wallet wallet = new Wallet(null, command.getUserId(), BigDecimal.ZERO, createdAt, updatedAt);

        return registerWalletGateway.execute(wallet);
    }
}
