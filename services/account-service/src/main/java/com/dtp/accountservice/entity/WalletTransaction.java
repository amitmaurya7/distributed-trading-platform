package com.dtp.accountservice.entity;

import com.dtp.accountservice.enums.TransactionStatus;
import com.dtp.accountservice.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_transactions")
@EntityListeners(AuditingEntityListener.class)
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Account and WalletTransaction belong to the same microservice,
     * therefore a JPA relationship is appropriate.
     */
    @NotNull(message = "Account is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "account_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_wallet_transactions_account"
            )
    )
    private Account account;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "transaction_type",
            nullable = false,
            length = 30
    )
    private TransactionType transactionType;

    @NotNull(message = "Transaction amount is required")
    @Positive(message = "Transaction amount must be greater than zero")
    @Column(
            name = "amount",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal amount;

    @NotNull(message = "Transaction status is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private TransactionStatus status = TransactionStatus.PENDING;

    @Size(
            max = 100,
            message = "Reference ID cannot exceed 100 characters"
    )
    @Column(
            name = "reference_id",
            length = 100
    )
    private String referenceId;

    @Size(
            max = 255,
            message = "Description cannot exceed 255 characters"
    )
    @Column(
            name = "description",
            length = 255
    )
    private String description;

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    protected WalletTransaction() {
        // Required by JPA
    }

    public WalletTransaction(
            Account account,
            TransactionType transactionType,
            BigDecimal amount,
            String referenceId,
            String description
    ) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
        this.referenceId = referenceId;
        this.description = description;
        this.status = TransactionStatus.PENDING;
    }

    public void complete() {
        ensurePending();
        this.status = TransactionStatus.COMPLETED;
    }

    public void fail() {
        ensurePending();
        this.status = TransactionStatus.FAILED;
    }

    public void cancel() {
        ensurePending();
        this.status = TransactionStatus.CANCELLED;
    }

    private void ensurePending() {
        if (this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException(
                    "Only a pending transaction can change status"
            );
        }
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}