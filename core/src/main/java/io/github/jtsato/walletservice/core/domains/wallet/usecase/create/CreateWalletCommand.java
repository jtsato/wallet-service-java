package io.github.jtsato.walletservice.core.domains.wallet.usecase.create;

import io.github.jtsato.walletservice.core.common.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString
public class CreateWalletCommand extends SelfValidating<CreateWalletCommand> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2882249634089711164L;

    @NotBlank(message = "validation.wallet.userId.blank")
    String userId;

    public CreateWalletCommand(final String userId) {
        this.userId = userId;
        validateSelf();
    }
}
