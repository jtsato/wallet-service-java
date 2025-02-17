package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.history;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class RetrieveHistoryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7019480436873286085L;

    private String initialDate;
    private String finalDate;
}
