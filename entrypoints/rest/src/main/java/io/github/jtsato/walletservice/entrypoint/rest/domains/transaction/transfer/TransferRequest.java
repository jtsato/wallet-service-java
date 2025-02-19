package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.transfer;

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
public class TransferRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5117326478260000000L;

    private Long destinationWalletId;

    private String amount;
}
