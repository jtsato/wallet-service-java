package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.create.RegisterWalletGateway;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RegisterWalletProvider implements RegisterWalletGateway {

    private static final WalletMapper walletMapper = Mappers.getMapper(WalletMapper.class);

    private final WalletRepository walletRepository;

    @Override
    public Wallet execute(final Wallet wallet) {
        final WalletEntity walletEntityToPersist = walletMapper.of(wallet);
        final WalletEntity walletEntity = walletRepository.save(walletEntityToPersist);
        return walletMapper.of(walletEntity);
    }
}
