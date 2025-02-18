package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.RegisterTransactionGateway;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RegisterTransactionProvider implements RegisterTransactionGateway {

    private static final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction execute(final Transaction transaction) {
        final TransactionEntity transactionEntityToPersist = transactionMapper.of(transaction);
        final TransactionEntity transactionEntity = transactionRepository.save(transactionEntityToPersist);

        return transactionMapper.of(transactionEntity);
    }
}
