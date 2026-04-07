package io.github.jtsato.walletservice.entrypoint.rest.support;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public final class ControllerTestFixture {

    private ControllerTestFixture() {}

    @Configuration
    public static class MessageSourceTestConfig {

        @Bean
        MessageSource messageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.setBasename("classpath:messages");
            source.setDefaultEncoding("UTF-8");
            return source;
        }
    }
}
