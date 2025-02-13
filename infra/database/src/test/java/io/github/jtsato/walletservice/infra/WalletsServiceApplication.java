package io.github.jtsato.walletservice.infra;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Jorge Takeshi Sato
 */

@EnableJpaRepositories(basePackageClasses = WalletsServiceApplication.class, repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
@SpringBootApplication
public class WalletsServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(WalletsServiceApplication.class, args);
    }
}
