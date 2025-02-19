package io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.DepositGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Jorge Takeshi Sato
 */

@Named
@RequiredArgsConstructor
public class DepositUseCaseImpl implements DepositUseCase {

    private final GetWalletByIdGateway getWalletByIdGateway;
    private final GetLocalDateTime getLocalDateTime;
    private final DepositGateway depositGateway;

    @Override
    public Wallet execute(final DepositCommand command) {

        final Optional<Wallet> optional = getWalletByIdGateway.execute(command.getWalletId());

        if (optional.isEmpty()){
            throw new NotFoundException("validation.wallet.id.notfound", String.valueOf(command.getWalletId()));
        }

        final Wallet currentWallet = optional.get();
        final LocalDateTime createdAt = getLocalDateTime.now();
        final BigDecimal amount = new BigDecimal(command.getAmount());
        final Transaction transaction = new Transaction(null, currentWallet, amount, Type.DEPOSIT, null, createdAt);

        return depositGateway.execute(currentWallet, transaction);
    }
}
