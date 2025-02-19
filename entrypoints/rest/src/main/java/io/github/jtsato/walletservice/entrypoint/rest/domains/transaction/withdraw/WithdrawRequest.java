package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.withdraw;

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
public class WithdrawRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1913943663663663663L;

    private String amount;
}
