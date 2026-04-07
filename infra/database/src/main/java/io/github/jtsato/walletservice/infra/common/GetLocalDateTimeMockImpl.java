package io.github.jtsato.walletservice.infra.common;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;

import java.time.LocalDateTime;

/** Not component-scanned; use {@code @Import} in tests that need a fixed clock. */
public class GetLocalDateTimeMockImpl implements GetLocalDateTime {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.parse("2025-02-12T22:04:59.123");
    }
}
