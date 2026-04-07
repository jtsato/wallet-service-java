package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.withdraw.WithdrawController;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.withdraw.WithdrawRequest;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Withdraw Controller Test")
@Import({
        WithdrawControllerTest.TestConfig.class,
        GlobalExceptionHandler.class,
        WalletsServiceExceptionHandler.class,
        ControllerTestFixture.MessageSourceTestConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = WithdrawController.class)
class WithdrawControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WithdrawUseCase withdrawUseCase;

    @Autowired
    private WebRequest webRequest;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public WithdrawUseCase mockWithdrawUseCase() {
            return Mockito.mock(WithdrawUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    private static String withdrawPath(final long walletId) {
        return "/v1/wallets/" + walletId + "/withdraws";
    }

    private static ResultMatcher jsonMessage(final String expected) {
        return jsonPath("$.message", is(expected));
    }

    @DisplayName("Successful to withdraw an amount")
    @Test
    void successfulToWithdrawAnAmount() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("cash.out@example.com");
        when(webRequest.getFullName()).thenReturn("Cash Out");

        final WithdrawCommand command = new WithdrawCommand(1L, "100.01");
        when(withdrawUseCase.execute(command)).thenReturn(new Wallet(1L, "red", BigDecimal.valueOf(100.01), LocalDateTime.parse("2025-02-14T22:04:59.123"), LocalDateTime.parse("2025-02-14T22:04:59.456")));

        final long walletId = 1L;
        mockMvc.perform(post(withdrawPath(walletId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildWithdrawRequest("100.01")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is("red")))
                .andExpect(jsonPath("$.balance", is(100.01)))
                .andExpect(jsonPath("$.createdAt", is("2025-02-14T22:04:59.123")))
                .andExpect(jsonPath("$.updatedAt", is("2025-02-14T22:04:59.456")));
    }

    @DisplayName("Not found when wallet has insufficient balance")
    @Test
    void notFoundWhenInsufficientBalance() throws Exception {
        when(webRequest.getEmail()).thenReturn("broke.user@example.com");
        when(webRequest.getFullName()).thenReturn("Broke User");

        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(withdrawPath(walletId));
        when(withdrawUseCase.execute(argThat(cmd -> cmd.getWalletId().equals(1L) && "500.00".equals(cmd.getAmount()))))
                .thenThrow(new NotFoundException("validation.wallet.insufficient.balance", "1"));

        mockMvc.perform(post(withdrawPath(walletId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildWithdrawRequest("500.00")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonMessage("Insufficient balance for wallet '1'."));
    }

    @DisplayName("Bad request when request body is missing")
    @Test
    void badRequestWhenBodyMissing() throws Exception {
        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(withdrawPath(walletId));

        mockMvc.perform(post(withdrawPath(walletId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String buildWithdrawRequest(final String amount) throws JsonProcessingException {
        final WithdrawRequest request = new WithdrawRequest(amount);
        return new ObjectMapper().writeValueAsString(request);
    }
}
