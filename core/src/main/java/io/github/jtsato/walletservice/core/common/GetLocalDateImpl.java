package io.github.jtsato.walletservice.core.common;

import jakarta.inject.Named;

import java.time.LocalDate;
import java.time.Clock;

/**
 * @author Jorge Takeshi Sato
 */

@Named
public class GetLocalDateImpl implements GetLocalDate {

    private static final Clock CLOCK = Clock.systemDefaultZone();

    @Override
    public LocalDate now() {
        return LocalDate.now(CLOCK);
    }
}
