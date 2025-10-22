package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.balance.RetrieveBalanceUseCase;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.balance.RetrieveBalanceController;
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
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Retrieve Balance Controller Test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RetrieveBalanceController.class)
class RetrieveBalanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RetrieveBalanceUseCase retrieveBalanceUseCase;

    @Autowired
    private WebRequest webRequest;

    @Autowired
    private MessageSource messageSource;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public RetrieveBalanceUseCase mockRetrieveBalanceUseCase() {
            return Mockito.mock(RetrieveBalanceUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }

        @Bean
        @Primary
        public MessageSource mockMessageSource() {
            return Mockito.mock(MessageSource.class);
        }
    }

    @DisplayName("Successful to get balance by wallet id")
    @Test
    void successfulToRetrieveBalance() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");

        // Arrange
        when(retrieveBalanceUseCase.execute(1L)).thenReturn(new Wallet(1L, "yellow", new BigDecimal("1001.01"), LocalDateTime.of(2021, 1, 1, 0, 0, 0), LocalDateTime.of(2021, 1, 1, 0, 0, 0)));

        // Act
        // Assert
        mockMvc.perform(get("/v1/wallets/1/balances")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.balance", is(1001.01)));
    }

    @DisplayName("Fail to get balance by wallet id when wallet is not found")
    @Test
    void failToRetrieveBalanceWhenWalletIdIsNotFound() throws Exception {

        // Arrange
        when(webRequest.getPath()).thenReturn("/v1/wallets/1/balances");
        when(messageSource.getMessage(eq("validation.wallet.id.notfound"), eq(new Object[]{"1"}), any(Locale.class)))
                .thenReturn("A carteira com o identificador '1' não foi encontrada!");
        when(retrieveBalanceUseCase.execute(1L))
                .thenThrow(new NotFoundException("validation.wallet.id.notfound", "1"));

        // Act
        // Assert
        mockMvc.perform(get("/v1/wallets/1/balances")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("A carteira com o identificador '1' não foi encontrada!")))
                .andExpect(jsonPath("$.status", is(404)));
    }
}
