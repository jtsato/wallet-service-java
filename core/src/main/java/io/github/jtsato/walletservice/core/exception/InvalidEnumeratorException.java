package io.github.jtsato.walletservice.core.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * @author Jorge Takeshi Sato
 */

@Getter
public class InvalidEnumeratorException extends CoreException {

    @Serial
    private static final long serialVersionUID = 6498777380767738701L;

    public InvalidEnumeratorException(final String message, final Object... args) {
        super(message, args);
    }
}
