package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import org.mapstruct.Mapper;

@Mapper
public interface WalletMapper {

    Wallet of(final WalletEntity walletEntity);
    WalletEntity of(final Wallet wallet);
}
