package io.github.jtsato.walletservice.infra.domains.transaction;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends EntityGraphJpaRepository<TransactionEntity, Long>, EntityGraphQuerydslPredicateExecutor<TransactionEntity> {

    Optional<TransactionEntity> findByWalletId(final Long walletId, final EntityGraph entityGraph);
}
