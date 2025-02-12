package io.github.jtsato.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Jorge Takeshi Sato
 */

@SpringBootApplication(scanBasePackageClasses = {WalletsServiceApplication.class})
public class WalletsServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(WalletsServiceApplication.class);
    }
}
