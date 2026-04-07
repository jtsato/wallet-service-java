package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.transfer.TransferController;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.transfer.TransferRequest;
import io.github.jtsato.walletservice.entrypoint.rest.support.ControllerTestFixture;
import io.github.jtsato.walletservice.exception.handler.GlobalExceptionHandler;
import io.github.jtsato.walletservice.exception.handler.WalletsServiceExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Transfer Controller Test")
@Import({
        TransferControllerTest.TestConfig.class,
        GlobalExceptionHandler.class,
        WalletsServiceExceptionHandler.class,
        ControllerTestFixture.MessageSourceTestConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransferUseCase transferUseCase;

    @Autowired
    private WebRequest webRequest;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public TransferUseCase mockTransferUseCase() {
            return Mockito.mock(TransferUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    private static String transferPath(final long originWalletId) {
        return "/v1/wallets/" + originWalletId + "/transfers";
    }

    private static ResultMatcher jsonMessageContaining(final String substring) {
        return jsonPath("$.message", containsString(substring));
    }

    private static ResultMatcher jsonMessage(final String expected) {
        return jsonPath("$.message", is(expected));
    }

    @DisplayName("Successful to transfer an amount")
    @Test
    void successfulToTransferAnAmount() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("payer.one@example.com");
        when(webRequest.getFullName()).thenReturn("Payer One");

        final long destinationWalletId = 2L;
        final String transferAmount = "100.01";
        final TransferCommand command = new TransferCommand(1L, destinationWalletId, transferAmount);
        when(transferUseCase.execute(command)).thenReturn(new Wallet(1L, "red", BigDecimal.valueOf(100), LocalDateTime.parse("2025-02-14T22:04:59.123"), LocalDateTime.parse("2025-02-14T22:04:59.456")));

        final long originWalletId = 1L;
        mockMvc.perform(post(transferPath(originWalletId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildTransferRequest(destinationWalletId, transferAmount)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is("red")))
                .andExpect(jsonPath("$.balance", is(100)))
                .andExpect(jsonPath("$.createdAt", is("2025-02-14T22:04:59.123")))
                .andExpect(jsonPath("$.updatedAt", is("2025-02-14T22:04:59.456")));
    }

    @DisplayName("Not found when destination wallet does not exist")
    @Test
    void notFoundWhenDestinationWalletMissing() throws Exception {
        when(webRequest.getEmail()).thenReturn("payer.two@example.com");
        when(webRequest.getFullName()).thenReturn("Payer Two");

        final long originWalletId = 1L;
        final long missingDestinationWalletId = 99L;
        final String transferAmountForMissingDestination = "77.77";
        when(webRequest.getPath()).thenReturn(transferPath(originWalletId));
        when(transferUseCase.execute(argThat(cmd -> cmd.getOriginWalletId().equals(1L)
                && cmd.getDestinationWalletId().equals(missingDestinationWalletId)
                && transferAmountForMissingDestination.equals(cmd.getAmount()))))
                .thenThrow(new NotFoundException("validation.wallet.id.notfound", "99"));

        mockMvc.perform(post(transferPath(originWalletId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                new TransferRequest(missingDestinationWalletId, transferAmountForMissingDestination))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonMessage("The wallet with the identifier '99' was not found!"));
    }

    @DisplayName("Bad request when transfer amount is invalid")
    @Test
    void badRequestWhenTransferAmountInvalid() throws Exception {
        when(webRequest.getEmail()).thenReturn("payer.three@example.com");
        when(webRequest.getFullName()).thenReturn("Payer Three");

        final long originWalletId = 1L;
        when(webRequest.getPath()).thenReturn(transferPath(originWalletId));

        mockMvc.perform(post(transferPath(originWalletId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new TransferRequest(2L, "0"))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonMessageContaining("The transfer amount must be at least 0.01!"));
    }

    @DisplayName("Bad request when request body is missing")
    @Test
    void badRequestWhenBodyMissing() throws Exception {
        final long originWalletId = 1L;
        when(webRequest.getPath()).thenReturn(transferPath(originWalletId));

        mockMvc.perform(post(transferPath(originWalletId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String buildTransferRequest(final long destinationWalletId, final String amount) throws JsonProcessingException {
        final TransferRequest request = new TransferRequest(destinationWalletId, amount);
        return new ObjectMapper().writeValueAsString(request);
    }
}
