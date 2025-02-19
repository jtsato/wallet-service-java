package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.deposit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class DepositRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5882249634089711164L;

    private Long walletId;
    private String amount;
}
