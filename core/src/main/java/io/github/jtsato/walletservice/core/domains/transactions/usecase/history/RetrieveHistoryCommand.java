package io.github.jtsato.walletservice.core.domains.transactions.usecase.history;

import io.github.jtsato.walletservice.core.common.validation.LocalDateTimeConstraint;
import io.github.jtsato.walletservice.core.common.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Jorge Takeshi Sato
 */

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString
public class RetrieveHistoryCommand extends SelfValidating<RetrieveHistoryCommand> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1913943663663663663L;

    @NotNull(message = "validation.wallet.id.null")
    private final Long walletId;

    @NotBlank(message = "validation.transaction.history.initial.date.blank")
    @LocalDateTimeConstraint(message = "validation.transaction.history.initial.date.invalid")
    private final String initialDate;

    @LocalDateTimeConstraint(message = "validation.transaction.history.final.date.invalid")
    private final String finalDate;

    public RetrieveHistoryCommand(final Long walletId, final String initialDate, final String finalDate) {
        this.walletId = walletId;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        validateSelf();
    }
}
