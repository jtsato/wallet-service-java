package io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit;

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
public class DepositCommand extends SelfValidating<DepositCommand> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1913943663663663663L;

    @NotNull(message = "validation.wallet.id.null")
    private final Long walletId;

    @NotNull(message = "validation.transaction.deposit.amount.null")
    @DecimalMin(value = "0.01", message = "validation.transaction.deposit.amount.invalid")
    private final BigDecimal amount;

    public DepositCommand(final Long walletId, final BigDecimal amount) {
        this.walletId = walletId;
        this.amount = amount;
        validateSelf();
    }
}
