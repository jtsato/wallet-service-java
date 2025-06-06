package io.github.jtsato.walletservice.infra.domains.transaction;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.common.paging.PageImpl;
import io.github.jtsato.walletservice.core.common.paging.Pageable;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.FindTransactionsByWalletIdAndPeriodGateway;
import io.github.jtsato.walletservice.infra.common.ListMapper;
import io.github.jtsato.walletservice.infra.common.PageRequestHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jorge Takeshi Sato
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FindTransactionsByWalletIdAndPeriodProvider implements FindTransactionsByWalletIdAndPeriodGateway {

    private static final ListMapper<Transaction, TransactionEntity> listMapper = new ListMapper<>() {};
    private static final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    private final TransactionRepository transactionRepository;

    @Override
    public Page<Transaction> execute(final Long walletId, final LocalDateTime startDate, final LocalDateTime endDate, final Integer pageNumber, final Integer size, final String orderBy) {

        final BooleanExpression predicate = buildPredicate(walletId, startDate, endDate);
        final PageRequest pageRequest = PageRequestHelper.buildPageRequest(pageNumber, size, sanitizeOrderBy(orderBy));

        final org.springframework.data.domain.Page<TransactionEntity> transactions = transactionRepository.findAll(predicate, pageRequest);
        final List<Transaction> content = listMapper.of(transactions.getContent(), transactionMapper::of);
        final Pageable pageable = new Pageable(pageNumber, size, content.size(), transactions.getTotalElements(), transactions.getTotalPages());

        return new PageImpl<>(content, pageable);
    }

    private BooleanExpression buildPredicate(final Long walletId, final LocalDateTime startDate, final LocalDateTime endDate) {

        final QTransactionEntity entity = QTransactionEntity.transactionEntity;
        BooleanExpression expression = entity.wallet.id.eq(walletId).or(entity.destinationWallet.id.eq(walletId));

        if (startDate != null) {
            expression = expression.and(entity.createdAt.goe(startDate));
        }
        if (endDate != null) {
            expression = expression.and(entity.createdAt.loe(endDate));
        }

        return expression;
    }

    private String sanitizeOrderBy(final String orderBy) {
        if (StringUtils.isBlank(orderBy) || StringUtils.equalsIgnoreCase(orderBy, "UNSORTED")) {
            return "createdAt:asc";
        }
        return StringUtils.stripToEmpty(orderBy);
    }
}
