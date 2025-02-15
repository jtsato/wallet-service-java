package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletResponse (
    Long id,
    String userId,
    BigDecimal balance,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
    implements Serializable {

    @Serial
    private static final long serialVersionUID = -2882249634089711164L;
}
