package io.github.jtsato.walletservice.entrypoint.rest.domains.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.create.CreateWalletCommand;
import io.github.jtsato.walletservice.core.domains.wallet.usecase.create.CreateWalletUseCase;
import io.github.jtsato.walletservice.entrypoint.rest.common.WebRequest;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.create.CreateWalletController;
import io.github.jtsato.walletservice.entrypoint.rest.domains.wallet.create.CreateWalletRequest;
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

@DisplayName("Create Wallet Controller Test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CreateWalletController.class)
class CreateWalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CreateWalletUseCase createWalletUseCase;

    @Autowired
    private WebRequest webRequest;

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public CreateWalletUseCase mockCreateWalletUseCase() {
            return Mockito.mock(CreateWalletUseCase.class);
        }

        @Bean
        @Primary
        public WebRequest mockWebRequest() {
            return Mockito.mock(WebRequest.class);
        }
    }

    @DisplayName("Successful to create a wallet")
    @Test
    void successfulToCreateAWallet() throws Exception {

        // Arrange
        when(webRequest.getEmail()).thenReturn("joe.doe.one@xyz.com");
        when(webRequest.getFullName()).thenReturn("Joe Doe");

        final CreateWalletCommand command = new CreateWalletCommand("red");
        when(createWalletUseCase.execute(command)).thenReturn(new Wallet(1L, "red", new BigDecimal("1001.01"), LocalDateTime.parse("2025-02-14T22:04:59.123"), LocalDateTime.parse("2025-02-14T22:04:59.456")));

        // Act
        // Assert
        mockMvc.perform(post("/v1/wallets").content(buildRequestBody()).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is("red")))
                .andExpect(jsonPath("$.balance", is(1001.01)))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());

        verify(createWalletUseCase, times(1)).execute(command);
        verifyNoMoreInteractions(createWalletUseCase);
    }

    private String buildRequestBody() throws JsonProcessingException {
        final CreateWalletRequest request = new CreateWalletRequest("red");
        return new ObjectMapper().writeValueAsString(request);
    }
}
