package io.github.jtsato.walletservice.entrypoint.rest.common;

import org.slf4j.Logger;

import java.util.function.Supplier;

public final class ControllerLogger {

    private ControllerLogger() {
    }

    public static void info(final Logger logger, final Supplier<String> messageSupplier) {
        if (logger.isInfoEnabled()) {
            logger.info(messageSupplier.get());
        }
    }
}
