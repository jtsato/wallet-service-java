package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WalletMapper {

    @Mapping(target = "withBalance", ignore = true)
    @Mapping(target = "withUpdatedAt", ignore = true)
    Wallet of(final WalletEntity walletEntity);
    WalletEntity of(final Wallet wallet);
}
