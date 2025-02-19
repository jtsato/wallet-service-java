package io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.WithdrawGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.DepositGateway;

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
public class WithdrawUseCaseImpl implements WithdrawUseCase {

    private final GetWalletByIdGateway getWalletByIdGateway;
    private final GetLocalDateTime getLocalDateTime;
    private final WithdrawGateway withdrawGateway;

    @Override
    public Wallet execute(final WithdrawCommand command) {

        final Wallet currentWallet = getWalletByIdGateway.execute(command.getWalletId()).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(command.getWalletId())));
        final BigDecimal amount = new BigDecimal(command.getAmount());

        if (currentWallet.balance().compareTo(amount) < 0) {
            throw new NotFoundException("validation.wallet.insufficient.balance", String.valueOf(command.getWalletId()));
        }

        final LocalDateTime createdAt = getLocalDateTime.now();
        final Transaction transaction = new Transaction(null, currentWallet, amount, Type.WITHDRAWAL, null, createdAt);

        return withdrawGateway.execute(currentWallet, transaction);
    }
}
