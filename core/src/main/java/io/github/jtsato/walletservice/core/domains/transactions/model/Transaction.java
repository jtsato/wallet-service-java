package io.github.jtsato.walletservice.core.domains.transactions.model;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Generated
public record Transaction (
    Long id,
    Long walletId,
    BigDecimal amount,
    Type type,
    LocalDateTime createdAt)
    implements Serializable {

    @Serial
    private static final long serialVersionUID = -2882249634089711164L;
}
