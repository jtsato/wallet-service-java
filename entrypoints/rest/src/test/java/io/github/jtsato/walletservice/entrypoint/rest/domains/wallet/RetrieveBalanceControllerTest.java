package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.balance.RetrieveBalanceUseCase;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.balance.RetrieveBalanceController;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Retrieve Balance Controller Test")
@Import({
        RetrieveBalanceControllerTest.TestConfig.class,
        GlobalExceptionHandler.class,
        WalletsServiceExceptionHandler.class,
        ControllerTestFixture.MessageSourceTestConfig.class
})
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

    @DisplayName("Not found when wallet does not exist")
    @Test
    void notFoundWhenWalletMissing() throws Exception {
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");
        when(webRequest.getPath()).thenReturn("/v1/wallets/42/balances");
        when(retrieveBalanceUseCase.execute(42L)).thenThrow(new NotFoundException("validation.wallet.id.notfound", "42"));

        mockMvc.perform(get("/v1/wallets/42/balances")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("The wallet with the identifier '42' was not found!")));
    }
}
