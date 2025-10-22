package io.github.jtsato.walletservice.entrypoint.rest.domains.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferUseCase;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.transfer.TransferController;
import io.github.jtsato.walletservice.entrypoint.rest.domains.transaction.transfer.TransferRequest;
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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Transfer Controller Test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransferUseCase transferUseCase;

    @Autowired
    private WebRequest webRequest;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public TransferUseCase mockTransferUseCase() {
            return Mockito.mock(TransferUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    @BeforeEach
    void setUp() {
        reset(transferUseCase, webRequest);
    }

    @DisplayName("Successful to transfer an amount")
    @Test
    void successfulToTransferAnAmount() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");

        final TransferCommand command = new TransferCommand(1L, 2L, "100.01");
        when(transferUseCase.execute(command)).thenReturn(new Wallet(1L, "red", BigDecimal.valueOf(100), LocalDateTime.parse("2025-02-14T22:04:59.123"), LocalDateTime.parse("2025-02-14T22:04:59.456")));

        // Act
        // Assert
        mockMvc.perform(post("/v1/wallets/1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildTransferRequest(new TransferRequest(2L, "100.01"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is("red")))
                .andExpect(jsonPath("$.balance", is(100)))
                .andExpect(jsonPath("$.createdAt", is("2025-02-14T22:04:59.123")))
                .andExpect(jsonPath("$.updatedAt", is("2025-02-14T22:04:59.456")));
    }

    @DisplayName("Fail to transfer when destination wallet does not exist")
    @Test
    void shouldReturnNotFoundWhenDestinationWalletDoesNotExist() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");
        when(webRequest.getPath()).thenReturn("/v1/wallets/1/transfers");

        final TransferCommand command = new TransferCommand(1L, 2L, "100.01");
        when(transferUseCase.execute(command)).thenThrow(new NotFoundException("validation.wallet.id.notfound", "2"));

        // Act
        // Assert
        mockMvc.perform(post("/v1/wallets/1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildTransferRequest(new TransferRequest(2L, "100.01"))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("The wallet with the identifier '2' was not found!")))
                .andExpect(jsonPath("$.path", is("/v1/wallets/1/transfers")));

        verify(transferUseCase, times(1)).execute(command);
        verifyNoMoreInteractions(transferUseCase);
    }

    @DisplayName("Fail to transfer when payload is invalid")
    @Test
    void shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {

        // Arrange
        when(webRequest.getPath()).thenReturn("/v1/wallets/1/transfers");

        // Act
        // Assert
        mockMvc.perform(post("/v1/wallets/1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildTransferRequest(new TransferRequest(null, "-1"))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", allOf(
                        containsString("The destination wallet identifier is required!"),
                        containsString("The transfer amount is invalid!"))))
                .andExpect(jsonPath("$.path", is("/v1/wallets/1/transfers")));

        verifyNoInteractions(transferUseCase);
    }

    private String buildTransferRequest(final TransferRequest request) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(request);
    }
}
