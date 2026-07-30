package io.github.jtsato.walletservice.core.common;

import jakarta.inject.Named;

import java.time.LocalDateTime;
import java.time.Clock;

/**
 * @author Jorge Takeshi Sato
 */

@Named
public class GetLocalDateTimeImpl implements GetLocalDateTime {

    private static final Clock CLOCK = Clock.systemDefaultZone();

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(CLOCK);
    }
}
