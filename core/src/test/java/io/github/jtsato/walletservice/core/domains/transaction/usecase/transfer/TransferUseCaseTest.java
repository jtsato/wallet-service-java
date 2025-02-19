package io.github.jtsato.walletservice.core.domains.transaction.usecase.transfer;

import io.github.jtsato.walletservice.core.common.GetLocalDateTime;
import io.github.jtsato.walletservice.core.domains.transactions.model.Transaction;
import io.github.jtsato.walletservice.core.domains.transactions.model.Type;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferCommand;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferUseCase;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.transfer.TransferUseCaseImpl;
import io.github.jtsato.walletservice.core.domains.transactions.usecase.xcutting.TransferGateway;
import io.github.jtsato.walletservice.core.domains.wallet.model.Wallet;
import io.github.jtsato.walletservice.core.domains.wallet.xcutting.GetWalletByIdGateway;
import io.github.jtsato.walletservice.core.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Transfer Use Case Test")
class TransferUseCaseTest {
    
    @Mock private final GetWalletByIdGateway getWalletByIdGateway = Mockito.mock(GetWalletByIdGateway.class);
    @Mock private final GetLocalDateTime getLocalDateTime = Mockito.mock(GetLocalDateTime.class);
    @Mock private final TransferGateway transferGateway = Mockito.mock(TransferGateway.class);

    @InjectMocks
    private final TransferUseCase useCase = new TransferUseCaseImpl(getWalletByIdGateway, getLocalDateTime, transferGateway);
    
    @DisplayName("Successfully to transfer")
    @Test
    void successfullyToTransfer() {
        
        // Arrange
        when(getWalletByIdGateway.execute(1L)).thenReturn(Optional.of(buildOriginWallet()));
        when(getWalletByIdGateway.execute(2L)).thenReturn(Optional.of(buildDestinationWallet()));
        when(getLocalDateTime.now()).thenReturn(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        when(transferGateway.execute(buildOriginWallet(), buildDestinationWallet(), buildTransaction())).thenReturn(buildUpdatedWallet());
        
        final TransferCommand command = new TransferCommand(1L, 2L, "10");
        
        // Act
        final Wallet wallet = useCase.execute(command);
        
        // Assert
        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.userId()).isEqualTo("1001");
        assertThat(wallet.balance()).isEqualTo(new BigDecimal(90));
        assertThat(wallet.createdAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
        assertThat(wallet.updatedAt()).isEqualTo(LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }
    
    @DisplayName("Fail to transfer if wallet not found")
    @Test
    void failToTransferIfWalletNotFound() {
        
        // Arrange
        when(getWalletByIdGateway.execute(1L)).thenReturn(Optional.empty());
        
        final TransferCommand command = new TransferCommand(1L, 2L, "10");
        
        // Act
        final Exception exception = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));
        
        // Assert
        assertThat(exception).isInstanceOf(NotFoundException.class);
        assertThat(exception.getMessage()).isEqualTo("validation.wallet.id.notfound");
    }
    
    private Wallet buildOriginWallet() {
        return new Wallet(1L, "1001", BigDecimal.valueOf(100), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }
    
    private Wallet buildDestinationWallet() {
        return new Wallet(2L, "1002", BigDecimal.valueOf(10), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }
    
    private Transaction buildTransaction() {
        return new Transaction(null, buildOriginWallet(), BigDecimal.TEN, Type.TRANSFER, buildDestinationWallet(), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }

    private Wallet buildUpdatedWallet() {
        return new Wallet(1L, "1001", BigDecimal.valueOf(90), LocalDateTime.parse("2020-03-12T22:04:59.123"), LocalDateTime.parse("2020-03-12T22:04:59.123"));
    }
}
