package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        String type,
        LocalDateTime createdAt
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 5723849152849173287L;
}
