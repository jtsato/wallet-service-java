package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.deposit;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.entrypoint.rest.common.JsonConverter;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.WalletPresenter;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.WalletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/*
 * A EntryPoint follows these steps:
 *
 * - Maps HTTP requests to Java objects
 * - Performs authorization checks
 * - Maps input to the input model of the use case
 * - Calls the use case
 * - Maps the output of the use case back to HTTP Returns an HTTP response
 */

/**
 * @author Jorge Takeshi Sato
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/wallets/{walletId}/deposits")
public class DepositController {
    
    private static final Logger log = LoggerFactory.getLogger(DepositController.class);

    private final DepositUseCase depositUseCase;
    private final WebRequest webRequest;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse execute(@PathVariable final Long walletId, @RequestBody @DefaultValue final DepositRequest request) {
        log.info("Controller -> DepositController by User: {}", webRequest.getEmail());
        final String json = JsonConverter.of(request);
        log.info("Controller -> DepositController -> with json: {}", json);
        final Wallet wallet = depositUseCase.execute(new DepositCommand(walletId, request.getAmount()));
        final WalletResponse response = WalletPresenter.of(wallet);
        log.info("Controller -> DepositController.execute with response: {}", response);

        return response;
    }
}
