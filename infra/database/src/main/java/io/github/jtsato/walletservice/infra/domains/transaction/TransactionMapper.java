package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.infra.domains.wallet.WalletMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {WalletMapper.class})
public interface TransactionMapper {

    Transaction of(final TransactionEntity transactionEntity);
    TransactionEntity of(final Transaction transaction);
}
