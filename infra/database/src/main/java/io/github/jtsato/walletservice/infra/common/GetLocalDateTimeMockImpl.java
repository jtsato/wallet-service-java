package io.github.jtsato.walletservice.infra.common;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import jakarta.inject.Named;

import java.time.LocalDateTime;

@Named
public class GetLocalDateTimeMockImpl implements GetLocalDateTime {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.parse("2025-02-12T22:04:59.123");
    }
}
