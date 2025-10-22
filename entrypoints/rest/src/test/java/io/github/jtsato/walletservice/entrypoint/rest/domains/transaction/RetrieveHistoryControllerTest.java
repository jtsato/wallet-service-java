package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.common.paging.PageImpl;
import io.github.jtsato.walletservice.core.common.paging.Pageable;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.history.RetrieveHistoryController;
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
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Retrieve History Controller Test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RetrieveHistoryController.class)
class RetrieveHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RetrieveHistoryUseCase retrieveHistoryUseCase;

    @Autowired
    private WebRequest webRequest;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public RetrieveHistoryUseCase mockRetrieveHistoryUseCase() {
            return Mockito.mock(RetrieveHistoryUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    @DisplayName("Successful to retrieve wallet history")
    @Test
    void successfulToRetrieveWalletHistory() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");

        final LocalDateTime createdAt = LocalDateTime.parse("2025-02-14T22:04:59.123");
        final Wallet wallet = new Wallet(1L, "yellow", new BigDecimal("200.01"), createdAt, createdAt);
        final Transaction transaction = new Transaction(10L, wallet, new BigDecimal("100.01"), Type.DEPOSIT, null, createdAt);
        final Pageable pageable = new Pageable(0, 1, 1, 1L, 1);
        final Page<Transaction> page = new PageImpl<>(List.of(transaction), pageable);

        when(retrieveHistoryUseCase.execute(eq(1L), eq("2025-02-14T22:04:59.123"), eq("2025-02-14T22:04:59.456"), eq(0), eq(1), anyString()))
                .thenReturn(page);

        // Act
        // Assert
        mockMvc.perform(get("/v1/wallets/{walletId}/balances/historical", 1)
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "createdAt,desc")
                        .param("startDate", "2025-02-14T22:04:59.123")
                        .param("endDate", "2025-02-14T22:04:59.456")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pageable.page", is(0)))
                .andExpect(jsonPath("$.pageable.size", is(1)))
                .andExpect(jsonPath("$.content[0].id", is(10)))
                .andExpect(jsonPath("$.content[0].amount", is(100.01)))
                .andExpect(jsonPath("$.content[0].type", is("DEPOSIT")))
                .andExpect(jsonPath("$.content[0].createdAt", is("2025-02-14T22:04:59.123")));
    }
}
