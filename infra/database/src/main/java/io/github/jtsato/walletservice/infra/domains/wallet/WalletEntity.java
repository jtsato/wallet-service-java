package io.github.jtsato.walletservice.infra.domains.wallet;

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
@Table(name = "WALLETS", indexes = {
        @Index(columnList = "USER_ID", name = "IDX_WALLETS_USER_ID"),
        @Index(columnList = "CREATED_AT", name = "IDX_WALLETS_CREATED_AT"),
        @Index(columnList = "UPDATED_AT", name = "IDX_WALLETS_UPDATED_AT"),
})
public class WalletEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2882249634089711164L;

    @Access(AccessType.PROPERTY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WALLET_ID", updatable = false, insertable = false)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "BALANCE", nullable = false)
    private BigDecimal balance;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

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
        final WalletEntity other = (WalletEntity) obj;
        if (id == null) {
            return other.id == null;
        } else
            return id.equals(other.id);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WalletEntity [");
        builder.append("id=").append(id);
        builder.append(", userId=").append(userId);
        builder.append(", balance=").append(balance);
        builder.append(", createdAt=").append(createdAt);
        builder.append(", updatedAt=").append(updatedAt);
        builder.append("]");

        return builder.toString();
    }
}
