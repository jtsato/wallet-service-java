package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.history;

import io.github.jtsato.walletservice.core.domains.transactions.usecase.history.RetrieveHistoryUseCase;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("Return bad request when start date is invalid")
    @Test
    void returnBadRequestWhenStartDateIsInvalid() throws Exception {

        when(webRequest.getPath()).thenReturn("/v1/wallets/1/balances/historical");

        mockMvc.perform(get("/v1/wallets/{walletId}/balances/historical", 1L)
                        .param("startDate", "2024-13-01"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("validation.transaction.history.start.date.invalid")));

        verifyNoInteractions(retrieveHistoryUseCase);
    }

    @DisplayName("Return bad request when start date is missing")
    @Test
    void returnBadRequestWhenStartDateIsMissing() throws Exception {

        when(webRequest.getPath()).thenReturn("/v1/wallets/1/balances/historical");

        mockMvc.perform(get("/v1/wallets/{walletId}/balances/historical", 1L))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("validation.transaction.history.start.date.blank")));

        verifyNoInteractions(retrieveHistoryUseCase);
    }
}
