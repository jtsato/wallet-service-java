package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.UpdateWalletByIdGateway;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UpdateWalletByIdProvider implements UpdateWalletByIdGateway {

    private static final WalletMapper walletMapper = Mappers.getMapper(WalletMapper.class);

    private final WalletRepository walletRepository;

    @Override
    public Optional<Wallet> execute(final Wallet wallet) {

        final Optional<WalletEntity> optional = walletRepository.findById(wallet.id());
        return optional.map(walletEntity -> updateWalletEntity(walletEntity, wallet));
    }

    private Wallet updateWalletEntity(final WalletEntity walletEntity, final Wallet wallet) {
        walletEntity.setBalance(wallet.balance());
        walletEntity.setUpdatedAt(wallet.updatedAt());
        return walletMapper.of(walletRepository.saveAndFlush(walletEntity));
    }
}
