package io.github.jtsato.walletservice.infra.domains.wallet;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface WalletRepository extends EntityGraphJpaRepository<WalletEntity, Long>, EntityGraphQuerydslPredicateExecutor<WalletEntity> {

    Optional<WalletEntity> findByUserId(final String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select wallet from WalletEntity wallet where wallet.id = :id")
    Optional<WalletEntity> findWithLockById(final Long id);
}
