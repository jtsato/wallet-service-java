package io.github.jtsato.walletservice.core.common;

import jakarta.inject.Named;

import java.time.LocalDateTime;

/**
 * @author Jorge Takeshi Sato
 */

@Named
public class GetLocalDateTimeImpl implements GetLocalDateTime {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
