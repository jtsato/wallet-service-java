package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.balance;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record BalanceResponse (
    BigDecimal balance
)
    implements Serializable {

    @Serial
    private static final long serialVersionUID = -7882249634089711164L;
}
