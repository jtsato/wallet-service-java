package io.github.jtsato.walletservice.core.exception;

import java.io.Serial;

public class InsufficientBalanceException extends CoreException {

    @Serial
    private static final long serialVersionUID = -8509103353339207443L;

    public InsufficientBalanceException(final Long walletId) {
        super("validation.wallet.insufficient.balance", String.valueOf(walletId));
    }
}
