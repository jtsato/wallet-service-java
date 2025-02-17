package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.history;

import io.github.jtsato.walletservice.entrypoint.rest.common.HttpStatusConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;

@Tag(name = "Transactions")
@FunctionalInterface
public interface RetrieveHistoryApiMethod {

    @Operation(summary = "Get transactions history by wallet id")

    @Parameter(name = "Accept-Language",
            example = "pt_BR",
            in = ParameterIn.HEADER,
            description = "Represents a specific geographical, political, or cultural region. Language & Country.")

    @PageableAsQueryParam

    @Parameter(in = ParameterIn.QUERY,
            name = "initialDate",
            description = "Filters transaction's creation date after the specified date. Format: ISO DATETIME",
            content = @Content(schema = @Schema(type = "string")))
    @Parameter(in = ParameterIn.QUERY,
            name = "finalDate",
            description = "Filters transaction's creation date before the specified date. Format: ISO DATETIME",
            content = @Content(schema = @Schema(type = "string")))

    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpStatusConstants.OK_200, description = HttpStatusConstants.OK_200_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.BAD_REQUEST_400, description = HttpStatusConstants.BAD_REQUEST_400_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.UNAUTHORIZED_401, description = HttpStatusConstants.UNAUTHORIZED_401_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.FORBIDDEN_403, description = HttpStatusConstants.FORBIDDEN_403_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.NOT_FOUND_404, description = HttpStatusConstants.NOT_FOUND_404_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.INTERNAL_SERVER_ERROR_500, description = HttpStatusConstants.INTERNAL_SERVER_ERROR_500_MESSAGE),
    })

    RetrieveHistoryWrapperResponse execute(@Parameter(hidden = true) final Pageable pageable,
                                           @Parameter(hidden = true) final Long walletId,
                                           @Parameter(hidden = true) final RetrieveHistoryRequest retrieveHistoryRequest);
}
