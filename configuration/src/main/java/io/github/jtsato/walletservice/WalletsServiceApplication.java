package io.github.jtsato.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;

/**
 * @author Jorge Takeshi Sato
 */

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = WalletsServiceApplication.class, repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
@EnableFeignClients
public class WalletsServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(WalletsServiceApplication.class, args);
    }
}
