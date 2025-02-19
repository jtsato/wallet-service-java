package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.deposit.DepositUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.deposit.DepositController;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.deposit.DepositRequest;
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

@DisplayName("Deposit Controller Test")
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

    @DisplayName("Successful to deposit an amount")
    @Test
    void successfulToDepositAnAmount() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");

        final DepositCommand command = new DepositCommand(1L, "100.01");
        when(depositUseCase.execute(command)).thenReturn(new Wallet(1L, "yellow", new BigDecimal("200.01"), LocalDateTime.parse("2025-02-14T22:04:59.123"), LocalDateTime.parse("2025-02-14T22:04:59.456")));

        // Act
        // Assert
        mockMvc.perform(post("/v1/wallets/1/deposits")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(buildDepositRequest()))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.userId", is("yellow")))
               .andExpect(jsonPath("$.balance", is(200.01)))
               .andExpect(jsonPath("$.createdAt", is("2025-02-14T22:04:59.123")))
               .andExpect(jsonPath("$.updatedAt", is("2025-02-14T22:04:59.456")));
    }

    private String buildDepositRequest() throws JsonProcessingException {
        final DepositRequest request = new DepositRequest("100.01");
        return new ObjectMapper().writeValueAsString(request);
    }
}
