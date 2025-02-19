package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.withdraw.WithdrawUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.withdraw.WithdrawController;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.withdraw.WithdrawRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Withdraw Controller Test")
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

    @DisplayName("Successful to withdraw an amount")
    @Test
    void successfulToWithdrawAnAmount() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");

        final WithdrawCommand command = new WithdrawCommand(1L, "100.01");
        when(withdrawUseCase.execute(command)).thenReturn(new Wallet(1L, "red", BigDecimal.valueOf(100.01), LocalDateTime.parse("2025-02-14T22:04:59.123"), LocalDateTime.parse("2025-02-14T22:04:59.456")));

        // Act
        // Assert
        mockMvc.perform(post("/v1/wallets/1/withdraws")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildWithdrawRequest()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is("red")))
                .andExpect(jsonPath("$.balance", is(100.01)))
                .andExpect(jsonPath("$.createdAt", is("2025-02-14T22:04:59.123")))
                .andExpect(jsonPath("$.updatedAt", is("2025-02-14T22:04:59.456")));
    }

    private String buildWithdrawRequest() throws JsonProcessingException {
        final WithdrawRequest request = new WithdrawRequest("100.01");
        return new ObjectMapper().writeValueAsString(request);
    }
}
