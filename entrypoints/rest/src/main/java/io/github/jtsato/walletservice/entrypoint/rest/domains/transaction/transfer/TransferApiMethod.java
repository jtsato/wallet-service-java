package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.transfer;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.entrypoint.rest.common.HttpStatusConstants;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.WalletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface TransferApiMethod {

    @Operation(summary = "Transfer value between wallets")

    @Parameter(name = "Accept-Language",
            example = "pt_BR",
            in = ParameterIn.HEADER,
            description = "Represents a specific geographical, political, or cultural region. Language & Country.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = HttpStatusConstants.CREATED_201, description = HttpStatusConstants.OK_200_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.BAD_REQUEST_400, description = HttpStatusConstants.BAD_REQUEST_400_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.UNAUTHORIZED_401, description = HttpStatusConstants.UNAUTHORIZED_401_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.FORBIDDEN_403, description = HttpStatusConstants.FORBIDDEN_403_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.NOT_FOUND_404, description = HttpStatusConstants.NOT_FOUND_404_MESSAGE),
            @ApiResponse(responseCode = HttpStatusConstants.INTERNAL_SERVER_ERROR_500, description = HttpStatusConstants.INTERNAL_SERVER_ERROR_500_MESSAGE),
    })

    WalletResponse execute(final Long originWalletId, final Long destinationWalletId, final TransferRequest transferRequest);
}
