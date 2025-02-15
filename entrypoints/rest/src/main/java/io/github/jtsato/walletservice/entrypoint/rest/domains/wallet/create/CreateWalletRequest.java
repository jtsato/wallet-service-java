package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.create;

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
public class CreateWalletRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2882249634089711164L;

    private String userId;
}
