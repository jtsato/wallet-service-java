package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.history;

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

    private String startDate;

    private String endDate;
}
