package io.github.jtsato.walletservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Profile("!test & !dev")
@EnableWebSecurity
@Configuration
public class ProductionSecurityConfiguration {

    @Bean
    public SecurityFilterChain productionSecurityFilterChain(final HttpSecurity http) {
        try {
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                            .anyRequest().authenticated())
                    .httpBasic(httpBasic -> {});
            return http.build();
        } catch (final Exception exception) {
            throw new IllegalStateException("Unable to configure production security", exception);
        }
    }
}
