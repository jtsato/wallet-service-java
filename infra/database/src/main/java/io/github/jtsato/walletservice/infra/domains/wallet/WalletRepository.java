package io.github.jtsato.walletservice.infra.domains.wallet;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends EntityGraphJpaRepository<WalletEntity, Long>, EntityGraphQuerydslPredicateExecutor<WalletEntity> {

    Optional<WalletEntity> findByUserId(final String userId, final EntityGraph entityGraph);
}
