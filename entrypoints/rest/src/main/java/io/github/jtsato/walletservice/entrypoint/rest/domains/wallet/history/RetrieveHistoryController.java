package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.history;

/*
 * A EntryPoint follows these steps:
 *
 * - Maps HTTP requests to Java objects
 * - Performs authorization checks
 * - Maps input to the input model of the use case
 * - Calls the use case
 * - Maps the output of the use case back to HTTP Returns an HTTP response
 */

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryUseCase;
import io.github.jtsato.walletservice.entrypoint.rest.common.JsonConverter;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.common.metric.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/wallets")
public class RetrieveHistoryController implements RetrieveHistoryApiMethod {

    private static final Logger log = LoggerFactory.getLogger(RetrieveHistoryController.class);

    private final RetrieveHistoryUseCase useCase;
    private final WebRequest webRequest;

    @Override
    @LogExecutionTime
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{walletId}/balances/historical")
    public RetrieveHistoryWrapperResponse execute(final Pageable pageable, @PathVariable final Long walletId, @DefaultValue final RetrieveHistoryRequest retrieveHistoryRequest) {

        final String jsonRequest = JsonConverter.of(retrieveHistoryRequest);
        log.info("Controller -> RetrieveHistoryController by User: {}", webRequest.getEmail());
        log.info("Controller -> RetrieveHistoryController with: {}", jsonRequest);

        final RetrieveHistoryCommand command = buildRetrieveHistoryCommand(walletId, retrieveHistoryRequest);
        final Page<Transaction> pageOfTransactions = useCase.execute(walletId, command.getInitialDate(), command.getFinalDate(), pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString());

        return RetrieveHistoryPresenter.of(pageOfTransactions);
    }

    private RetrieveHistoryCommand buildRetrieveHistoryCommand(final Long walletId, RetrieveHistoryRequest retrieveHistoryRequest) {
        return new RetrieveHistoryCommand(walletId, retrieveHistoryRequest.getInitialDate(), retrieveHistoryRequest.getFinalDate());
    }
}
