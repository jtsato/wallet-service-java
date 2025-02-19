package io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.TransferGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Jorge Takeshi Sato
 */

@Named
@RequiredArgsConstructor
public class TransferUseCaseImpl implements TransferUseCase {

    private final GetWalletByIdGateway getWalletByIdGateway;
    private final GetLocalDateTime getLocalDateTime;
    private final TransferGateway transferGateway;

    @Override
    public Wallet execute(final TransferCommand command) {

        final Wallet originWallet = getWalletByIdGateway.execute(command.getOriginWalletId()).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(command.getOriginWalletId())));
        final Wallet destinationWallet = getWalletByIdGateway.execute(command.getDestinationWalletId()).orElseThrow(() -> new NotFoundException("validation.wallet.id.notfound", String.valueOf(command.getDestinationWalletId())));
        final BigDecimal amount = new BigDecimal(command.getAmount());

        if (originWallet.balance().compareTo(amount) < 0) {
            throw new NotFoundException("validation.wallet.insufficient.balance", String.valueOf(command.getOriginWalletId()));
        }

        final LocalDateTime createdAt = getLocalDateTime.now();
        final Transaction transaction = new Transaction(null, originWallet, amount, Type.TRANSFER, destinationWallet, createdAt);

        return transferGateway.execute(originWallet, destinationWallet, transaction);
    }
}
