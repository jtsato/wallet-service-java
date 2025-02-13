package io.github.jtsato.walletservice.core.domains.wallet.model;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Generated
public record Wallet(
    Long id,
    String userId,
    BigDecimal balance,
    LocalDateTime createdAt)
    implements Serializable {

    @Serial
    private static final long serialVersionUID = -2882249634089711164L;
}
