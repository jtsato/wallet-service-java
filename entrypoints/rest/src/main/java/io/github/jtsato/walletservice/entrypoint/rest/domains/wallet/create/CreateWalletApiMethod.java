package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.create;

import io.github.jtsato.walletservice.entrypoint.rest.common.HttpStatusConstants;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.WalletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Wallets")
@FunctionalInterface
public interface CreateWalletApiMethod {

    @Operation(summary = "Create a wallet")

    @Parameter(name = "Accept-Language",
            example = "pt_BR",
            description = "Represents a specific geographical, political, or cultural region. Language & Country.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpStatusConstants.CREATED_201, description = HttpStatusConstants.CREATED_201_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.BAD_REQUEST_400, description = HttpStatusConstants.BAD_REQUEST_400_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.UNAUTHORIZED_401, description = HttpStatusConstants.UNAUTHORIZED_401_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.FORBIDDEN_403, description = HttpStatusConstants.FORBIDDEN_403_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.CONFLICT_409, description = HttpStatusConstants.CONFLICT_409_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.INTERNAL_SERVER_ERROR_500, description = HttpStatusConstants.INTERNAL_SERVER_ERROR_500_MESSAGE),
    })
    WalletResponse execute(final CreateWalletRequest request);
}
