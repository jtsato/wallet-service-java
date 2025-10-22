package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.history;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.common.paging.PageImpl;
import io.github.jtsato.walletservice.core.common.paging.Pageable;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

    @BeforeEach
    void setUp() {
        reset(retrieveHistoryUseCase, webRequest);
    }

    @DisplayName("Successful to retrieve paginated transaction history")
    @Test
    void shouldReturnPagedHistorySuccessfully() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");

        final Wallet wallet = new Wallet(1L, "john", new BigDecimal("250.00"), LocalDateTime.parse("2024-01-01T10:00:00"), LocalDateTime.parse("2024-01-01T10:00:00"));
        final Transaction transaction = new Transaction(10L, wallet, new BigDecimal("25.50"), Type.DEPOSIT, null, LocalDateTime.parse("2024-01-05T10:15:30"));
        final Page<Transaction> page = new PageImpl<>(List.of(transaction), new Pageable(1, 2, 1, 1L, 1));

        when(retrieveHistoryUseCase.execute(1L, "2024-01-01T00:00:00", "2024-01-31T23:59:59", 1, 2, "createdAt: DESC")).thenReturn(page);

        // Act
        // Assert
        mockMvc.perform(get("/v1/wallets/1/balances/historical")
                        .param("startDate", "2024-01-01T00:00:00")
                        .param("endDate", "2024-01-31T23:59:59")
                        .param("page", "1")
                        .param("size", "2")
                        .param("sort", "createdAt,desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id", is(10)))
                .andExpect(jsonPath("$.content[0].amount", is(25.5)))
                .andExpect(jsonPath("$.content[0].type", is("DEPOSIT")))
                .andExpect(jsonPath("$.content[0].createdAt", is("2024-01-05T10:15:30")))
                .andExpect(jsonPath("$.pageable.page", is(1)))
                .andExpect(jsonPath("$.pageable.size", is(2)))
                .andExpect(jsonPath("$.pageable.numberOfElements", is(1)))
                .andExpect(jsonPath("$.pageable.totalOfElements", is(1)))
                .andExpect(jsonPath("$.pageable.totalPages", is(1)));

        verify(retrieveHistoryUseCase, times(1)).execute(eq(1L), eq("2024-01-01T00:00:00"), eq("2024-01-31T23:59:59"), eq(1), eq(2), eq("createdAt: DESC"));
        verifyNoMoreInteractions(retrieveHistoryUseCase);
    }

    @DisplayName("Fail to retrieve history when start date is invalid")
    @Test
    void shouldReturnBadRequestWhenStartDateIsInvalid() throws Exception {

        // Arrange
        when(webRequest.getPath()).thenReturn("/v1/wallets/1/balances/historical");

        // Act
        // Assert
        mockMvc.perform(get("/v1/wallets/1/balances/historical")
                        .param("startDate", "2024-13-01T00:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("The initial date is invalid!")))
                .andExpect(jsonPath("$.path", is("/v1/wallets/1/balances/historical")));

        verifyNoInteractions(retrieveHistoryUseCase);
    }

    @DisplayName("Fail to retrieve history when start date is missing")
    @Test
    void shouldReturnBadRequestWhenStartDateIsMissing() throws Exception {

        // Arrange
        when(webRequest.getPath()).thenReturn("/v1/wallets/1/balances/historical");

        // Act
        // Assert
        mockMvc.perform(get("/v1/wallets/1/balances/historical")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("The initial date is required!")))
                .andExpect(jsonPath("$.path", is("/v1/wallets/1/balances/historical")));

        verifyNoInteractions(retrieveHistoryUseCase);
    }
}
