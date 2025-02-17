package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.balance;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.balance.RetrieveBalanceUseCase;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.common.metric.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/*
 * A EntryPoint follows these steps:
 *
 * - Maps HTTP requests to Java objects
 * - Performs authorization checks
 * - Maps input to the input model of the use case
 * - Calls the use case
 * - Maps the output of the use case back to HTTP Returns an HTTP response
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/wallets")
public class RetrieveBalanceController implements RetrieveBalanceApiMethod {

    private static final Logger log = LoggerFactory.getLogger(RetrieveBalanceController.class);

    private final RetrieveBalanceUseCase useCase;
    private final WebRequest webRequest;

    @Override
    @LogExecutionTime
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/balances")
    public BalanceResponse execute(@PathVariable final Long id) {
        log.info("Controller -> RetrieveBalanceController by User: {}", webRequest.getEmail());
        log.info("Controller -> RetrieveBalanceController by Wallet Id: {}", id);
        final Wallet wallet = useCase.execute(id);
        log.info("Controller -> RetrieveBalanceController.execute with response: {}", wallet.balance());

        return new BalanceResponse(wallet.balance());
    }
}
