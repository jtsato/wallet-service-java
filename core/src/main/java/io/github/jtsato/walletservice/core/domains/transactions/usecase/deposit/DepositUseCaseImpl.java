package io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.RegisterTransactionGateway;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.UpdateWalletByIdGateway;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Jorge Takeshi Sato
 */

@Named
@RequiredArgsConstructor
public class DepositUseCaseImpl implements DepositUseCase {

    private final GetWalletByIdGateway getWalletByIdGateway;
    private final GetLocalDateTime getLocalDateTime;
    private final RegisterTransactionGateway registerTransactionGateway;
    private final UpdateWalletByIdGateway updateWalletByIdGateway;

    @Override
    public Wallet execute(final DepositCommand command) {

        final Optional<Wallet> optional = getWalletByIdGateway.execute(command.getWalletId());

        if (optional.isEmpty()){
            throw new NotFoundException("validation.wallet.id.notfound", String.valueOf(command.getWalletId()));
        }

        final Wallet currentWallet = optional.get();

        final Transaction transaction = registerTransactionGateway.execute(new Transaction(null, currentWallet, command.getAmount(), Type.DEPOSIT, getLocalDateTime.now()));
        final Wallet wallet = currentWallet.withBalance(currentWallet.balance().add(transaction.amount())).withUpdatedAt(getLocalDateTime.now());

        return updateWalletByIdGateway.execute(wallet).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(currentWallet.id())));
    }
}
