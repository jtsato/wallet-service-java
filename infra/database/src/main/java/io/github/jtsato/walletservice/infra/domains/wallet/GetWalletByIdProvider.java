package io.github.jtsato.walletservice.infra.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GetWalletByIdProvider implements GetWalletByIdGateway {

    private static final WalletMapper walletMapper = Mappers.getMapper(WalletMapper.class);

    private final WalletRepository walletRepository;

    @Override
    public Optional<Wallet> execute(final Long id) {
        final Optional<WalletEntity> optional = walletRepository.findById(id);
        return optional.map(walletMapper::of);
    }
}
