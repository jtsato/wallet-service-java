package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.deposit.DepositController;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.deposit.DepositRequest;
import io.github.jtsato.walletservice.entrypoint.rest.support.ControllerTestFixture;
import io.github.jtsato.walletservice.exception.handler.GlobalExceptionHandler;
import io.github.jtsato.walletservice.exception.handler.WalletsServiceExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Deposit Controller Test")
@Import({
        DepositControllerTest.TestConfig.class,
        GlobalExceptionHandler.class,
        WalletsServiceExceptionHandler.class,
        ControllerTestFixture.MessageSourceTestConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DepositController.class)
class DepositControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepositUseCase depositUseCase;

    @Autowired
    private WebRequest webRequest;

    @BeforeEach
    void resetDepositUseCaseInvocations() {
        clearInvocations(depositUseCase);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public DepositUseCase mockDepositUseCase() {
            return Mockito.mock(DepositUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    private static String depositPath(final long walletId) {
        return "/v1/wallets/" + walletId + "/deposits";
    }

    private static ResultMatcher jsonMessage(final String expected) {
        return jsonPath("$.message", is(expected));
    }

    @DisplayName("Successful to deposit an amount")
    @Test
    void successfulToDepositAnAmount() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("nora.deposit@example.com");
        when(webRequest.getFullName()).thenReturn("Nora Deposit");

        final String depositAmount = "100.01";
        final DepositCommand command = new DepositCommand(1L, depositAmount);
        when(depositUseCase.execute(command)).thenReturn(new Wallet(1L, "yellow", new BigDecimal("200.01"), LocalDateTime.parse("2025-02-14T22:04:59.123"), LocalDateTime.parse("2025-02-14T22:04:59.456")));

        final long walletId = 1L;
        mockMvc.perform(post(depositPath(walletId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(buildDepositRequest(depositAmount)))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.userId", is("yellow")))
               .andExpect(jsonPath("$.balance", is(200.01)))
               .andExpect(jsonPath("$.createdAt", is("2025-02-14T22:04:59.123")))
               .andExpect(jsonPath("$.updatedAt", is("2025-02-14T22:04:59.456")));
    }

    @DisplayName("Bad request when request body is missing")
    @Test
    void badRequestWhenBodyMissing() throws Exception {
        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(depositPath(walletId));

        mockMvc.perform(post(depositPath(walletId))
                        .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonMessage("The required request body is invalid!"));

        verifyNoInteractions(depositUseCase);
    }

    @DisplayName("Not found when wallet does not exist")
    @Test
    void notFoundWhenWalletMissing() throws Exception {
        when(webRequest.getEmail()).thenReturn("marcus.lost@example.com");
        when(webRequest.getFullName()).thenReturn("Marcus Lost");

        final long walletId = 99L;
        when(webRequest.getPath()).thenReturn(depositPath(walletId));
        final String depositAmountForMissingWallet = "50.25";
        when(depositUseCase.execute(argThat(cmd -> cmd.getWalletId().equals(99L) && depositAmountForMissingWallet.equals(cmd.getAmount()))))
                .thenThrow(new NotFoundException("validation.wallet.id.notfound", "99"));

        mockMvc.perform(post(depositPath(walletId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildDepositRequest(depositAmountForMissingWallet)))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(jsonMessage("The wallet with the identifier '99' was not found!"));
    }

    @DisplayName("Bad request when deposit amount is invalid")
    @Test
    void badRequestWhenDepositAmountInvalid() throws Exception {
        when(webRequest.getEmail()).thenReturn("ivy.zero@example.com");
        when(webRequest.getFullName()).thenReturn("Ivy Zero");

        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(depositPath(walletId));

        mockMvc.perform(post(depositPath(walletId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new DepositRequest("0"))))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonMessage("The deposit amount must be at least 0.01!"));

        verifyNoInteractions(depositUseCase);
    }

    @DisplayName("Invalid deposit amount message is localized in Portuguese")
    @Test
    void invalidDepositAmountMessageInPortuguese() throws Exception {
        when(webRequest.getEmail()).thenReturn("joao.silva@example.br");
        when(webRequest.getFullName()).thenReturn("João Silva");

        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(depositPath(walletId));

        mockMvc.perform(post(depositPath(walletId))
                        .locale(Locale.of("pt", "BR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new DepositRequest("0"))))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(jsonMessage("O valor do depósito deve ser no mínimo 0,01!"));
    }

    private String buildDepositRequest(final String amount) throws JsonProcessingException {
        final DepositRequest request = new DepositRequest(amount);
        return new ObjectMapper().writeValueAsString(request);
    }
}
