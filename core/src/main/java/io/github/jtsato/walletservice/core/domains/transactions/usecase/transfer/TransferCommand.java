package io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer;

import io.github.jtsato.walletservice.core.common.validation.SelfValidating;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString
public class TransferCommand extends SelfValidating<TransferCommand> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1913943663663663663L;

    @NotNull(message = "validation.source.wallet.id.null")
    private final Long sourceWalletId;

    @NotNull(message = "validation.destination.wallet.id.null")
    private final Long destinationWalletId;

    @NotNull(message = "validation.transaction.transfer.amount.null")
    @DecimalMin(value = "0.01", message = "validation.transaction.transfer.amount.invalid")
    private final BigDecimal amount;

    public TransferCommand(final Long sourceWalletId, final Long destinationWalletId, final BigDecimal amount) {
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        this.amount = amount;
        validateSelf();
    }
}
