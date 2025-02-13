package io.github.jtsato.walletservice.core.common;

import jakarta.inject.Named;

import java.time.LocalDate;

/**
 * @author Jorge Takeshi Sato
 */

@Named
public class GetLocalDateImpl implements GetLocalDate {

    @Override
    public LocalDate now() {
        return LocalDate.now();
    }
}
