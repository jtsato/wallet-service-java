package io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw;

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

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString
public class WithdrawCommand extends SelfValidating<WithdrawCommand> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1913943663663663663L;

    @NotNull(message = "validation.wallet.id.null")
    private final Long walletId;

    @NotNull(message = "validation.transaction.withdraw.amount.null")
    @DecimalMin(value = "0.01", message = "validation.transaction.withdraw.amount.invalid")
    private final String amount;

    public WithdrawCommand(final Long walletId, final String amount) {
        this.walletId = walletId;
        this.amount = amount;
        validateSelf();
    }
}
