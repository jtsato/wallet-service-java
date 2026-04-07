package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.history;

import io.github.jtsato.walletservice.core.common.paging.Page;
import io.github.jtsato.walletservice.core.common.paging.PageImpl;
import io.github.jtsato.walletservice.core.common.paging.Pageable;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.support.ControllerTestFixture;
import io.github.jtsato.walletservice.exception.handler.GlobalExceptionHandler;
import io.github.jtsato.walletservice.exception.handler.WalletsServiceExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Retrieve History Controller Test")
@Import({
        RetrieveHistoryControllerTest.TestConfig.class,
        RetrieveHistoryControllerTest.PageableWebConfig.class,
        GlobalExceptionHandler.class,
        WalletsServiceExceptionHandler.class,
        ControllerTestFixture.MessageSourceTestConfig.class
})
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
        RetrieveHistoryUseCase retrieveHistoryUseCase() {
            return Mockito.mock(RetrieveHistoryUseCase.class);
        }

        @Bean
        @Primary
        WebRequest webRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    @TestConfiguration
    static class PageableWebConfig implements WebMvcConfigurer {

        @Override
        public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new PageableHandlerMethodArgumentResolver());
        }
    }

    private static final String QUERY_PARAM_START_DATE = "startDate";
    private static final String QUERY_PARAM_END_DATE = "endDate";

    private static String historicalPath(final long walletId) {
        return "/v1/wallets/" + walletId + "/balances/historical";
    }

    private static ResultMatcher jsonMessage(final String expected) {
        return jsonPath("$.message", is(expected));
    }

    @DisplayName("Successful to retrieve history when parameters are valid")
    @Test
    void successfulToRetrieveHistory() throws Exception {
        when(webRequest.getEmail()).thenReturn("history.reader@example.com");
        when(webRequest.getFullName()).thenReturn("History Reader");

        final Wallet wallet = new Wallet(1L, "red", BigDecimal.TEN, LocalDateTime.parse("2025-02-01T10:00:00"), LocalDateTime.parse("2025-02-01T10:00:00"));
        final Transaction tx = new Transaction(1L, wallet, new BigDecimal("50.00"), Type.DEPOSIT, null, LocalDateTime.parse("2025-02-05T12:00:00"));
        final Pageable pageable = new Pageable(0, 20, 1, 1L, 1);
        final Page<Transaction> page = new PageImpl<>(List.of(tx), pageable);

        final long walletId = 1L;
        final String periodStart = "2025-02-01T00:00:00";
        final String periodEnd = "2025-02-10T00:00:00";

        when(retrieveHistoryUseCase.execute(eq(walletId), eq(periodStart), eq(periodEnd), eq(0), eq(20), anyString()))
                .thenReturn(page);

        mockMvc.perform(get(historicalPath(walletId))
                        .param(QUERY_PARAM_START_DATE, periodStart)
                        .param(QUERY_PARAM_END_DATE, periodEnd)
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].amount", is(50.0)));

        verify(retrieveHistoryUseCase).execute(eq(walletId), eq(periodStart), eq(periodEnd), eq(0), eq(20), anyString());
    }

    @DisplayName("Bad request when start date is missing")
    @Test
    void badRequestWhenStartDateMissing() throws Exception {
        when(webRequest.getEmail()).thenReturn("audit.gap@example.com");
        when(webRequest.getFullName()).thenReturn("Audit Gap");

        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(historicalPath(walletId));

        mockMvc.perform(get(historicalPath(walletId))
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonMessage("The initial date is required!"));
    }

    @DisplayName("Bad request when start date is invalid")
    @Test
    void badRequestWhenStartDateInvalid() throws Exception {
        when(webRequest.getEmail()).thenReturn("bad.date@example.com");
        when(webRequest.getFullName()).thenReturn("Bad Date");

        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(historicalPath(walletId));

        mockMvc.perform(get(historicalPath(walletId))
                        .param(QUERY_PARAM_START_DATE, "not-a-date")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonMessage("The initial date is invalid!"));
    }

    @DisplayName("Bad request when end date is invalid")
    @Test
    void badRequestWhenEndDateInvalid() throws Exception {
        when(webRequest.getEmail()).thenReturn("range.fail@example.com");
        when(webRequest.getFullName()).thenReturn("Range Fail");

        final long walletId = 1L;
        when(webRequest.getPath()).thenReturn(historicalPath(walletId));
        final String alternateStart = "2025-06-01T12:00:00";

        mockMvc.perform(get(historicalPath(walletId))
                        .param(QUERY_PARAM_START_DATE, alternateStart)
                        .param(QUERY_PARAM_END_DATE, "invalid")
                        .param("page", "0")
                        .param("size", "20")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonMessage("The final date is invalid!"));
    }
}
