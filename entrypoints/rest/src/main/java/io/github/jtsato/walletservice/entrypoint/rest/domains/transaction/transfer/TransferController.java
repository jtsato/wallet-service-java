package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.transfer;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferUseCase;
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
@RequestMapping("v1/wallets/{originWalletId}/transfers")
public class TransferController {
    
    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    private final TransferUseCase transferUseCase;
    private final WebRequest webRequest;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse execute(@PathVariable final Long originWalletId, @RequestBody @DefaultValue final TransferRequest request) {
        log.info("Controller -> TransferController by User: {}", webRequest.getEmail());
        final String json = JsonConverter.of(request);
        log.info("Controller -> TransferController -> with json: {}", json);
        final Wallet wallet = transferUseCase.execute(new TransferCommand(originWalletId, request.getDestinationWalletId(), request.getAmount()));
        final WalletResponse response = WalletPresenter.of(wallet);
        log.info("Controller -> TransferController.execute with response: {}", response);

        return response;
    }
}
