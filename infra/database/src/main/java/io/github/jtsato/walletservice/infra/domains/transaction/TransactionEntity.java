package io.github.jtsato.walletservice.infra.domains.transaction;

import io.github.jtsato.walletservice.infra.domains.wallet.WalletEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRANSACTIONS", indexes = {
        @Index(columnList = "WALLET_ID", name = "IDX_TRANSACTIONS_WALLET_ID"),
        @Index(columnList = "TYPE_", name = "IDX_TRANSACTIONS_TYPE_"),
        @Index(columnList = "CREATED_AT", name = "IDX_TRANSACTIONS_CREATED_AT"),
})
public class TransactionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2882249634089711164L;

    @Access(AccessType.PROPERTY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID", updatable = false, insertable = false)
    private Long id;

    @JoinColumn(name = "WALLET_ID", foreignKey = @ForeignKey(name = "FK_TRANSACTIONS_WALLET_ID"))
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private WalletEntity wallet;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "TYPE_", nullable = false)
    private String type;

    @JoinColumn(name = "DESTINATION_WALLET_ID", foreignKey = @ForeignKey(name = "FK_TRANSACTIONS_DESTINATION_WALLET_ID"))
    @ManyToOne(fetch = FetchType.LAZY)
    private WalletEntity destinationWallet;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Override
    public int hashCode() {
        final int prime = 23;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TransactionEntity other = (TransactionEntity) obj;
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TransactionEntity [");
        builder.append("id=").append(id);
        builder.append(", wallet=").append(wallet);
        builder.append(", amount=").append(amount);
        builder.append(", type=").append(type);
        builder.append(", createdAt=").append(createdAt);
        builder.append("]");

        return builder.toString();
    }
}
