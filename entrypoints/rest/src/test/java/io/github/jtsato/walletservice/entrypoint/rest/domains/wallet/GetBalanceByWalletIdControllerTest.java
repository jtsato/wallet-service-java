package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet;

import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.balance.GetBalanceByWalletIdUseCase;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.balance.GetBalanceByWalletIdController;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Get Balance By Wallet Id Controller Test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GetBalanceByWalletIdController.class)
class GetBalanceByWalletIdControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GetBalanceByWalletIdUseCase getBalanceByWalletIdUseCase;

    @Autowired
    private WebRequest webRequest;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public GetBalanceByWalletIdUseCase mockGetBalanceByWalletIdUseCase() {
            return Mockito.mock(GetBalanceByWalletIdUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    @DisplayName("Successful to get balance by wallet id")
    @Test
    void successfulToGetBalanceByWalletId() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");

        // Arrange
        when(getBalanceByWalletIdUseCase.execute(1L)).thenReturn(new Wallet(1L, "yellow", BigDecimal.ZERO, LocalDateTime.of(2021, 1, 1, 0, 0, 0), LocalDateTime.of(2021, 1, 1, 0, 0, 0)));

        // Act
        // Assert
        mockMvc.perform(get("/v1/wallets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.balance", is(0)))
                .andExpect(jsonPath("$.createdAt", is("2021-01-01T00:00:00")))
                .andExpect(jsonPath("$.updatedAt", is("2021-01-01T00:00:00")));

    }
}
