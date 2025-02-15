package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.create;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.create.CreateWalletCommand;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.create.CreateWalletUseCase;
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
@RequestMapping("v1/wallets")
public class CreateWalletController implements CreateWalletApiMethod {

    private static final Logger log = LoggerFactory.getLogger(CreateWalletController.class);

    private final CreateWalletUseCase createWalletUseCase;
    private final WebRequest webRequest;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse execute(@RequestBody @DefaultValue final CreateWalletRequest request) {
        log.info("Controller -> RegisterWalletController by User: {}", webRequest.getEmail());
        final String json = JsonConverter.of(request);
        log.info("Controller -> RegisterBetController -> with json: {}", json);
        final CreateWalletCommand command = new CreateWalletCommand(request.getUserId());
        final Wallet wallet = createWalletUseCase.execute(command);
        final WalletResponse response = WalletPresenter.of(wallet);
        log.info("Controller -> RegisterWalletController.execute with response: {}", response);

        return response;
    }
}
