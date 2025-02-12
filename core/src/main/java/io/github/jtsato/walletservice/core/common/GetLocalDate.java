package io.github.jtsato.walletservice.core.common;

import java.time.LocalDate;

/**
 * @author Jorge Takeshi Sato
 */

@FunctionalInterface
public interface GetLocalDate {

    LocalDate now();
}
