package com.dtp.accountservice.entity;

import com.dtp.accountservice.enums.AccountStatus;
import com.dtp.accountservice.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * User belongs to user-service, so we store only its ID.
     * Do not create a @ManyToOne relationship with User here.
     */
    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "Account number is required")
    @Size(max = 50, message = "Account number cannot exceed 50 characters")
    @Column(
            name = "account_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String accountNumber;

    @NotNull(message = "Account type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 30)
    private AccountType accountType;

    @NotNull(message = "Account status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AccountStatus status = AccountStatus.ACTIVE;

    @NotNull(message = "Available balance is required")
    @PositiveOrZero(message = "Available balance cannot be negative")
    @Column(
            name = "available_balance",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @NotNull(message = "Blocked balance is required")
    @PositiveOrZero(message = "Blocked balance cannot be negative")
    @Column(
            name = "blocked_balance",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal blockedBalance = BigDecimal.ZERO;

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected Account() {
        // Required by JPA
    }

    public Account(
            Long userId,
            String accountNumber,
            AccountType accountType
    ) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.status = AccountStatus.ACTIVE;
        this.availableBalance = BigDecimal.ZERO;
        this.blockedBalance = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getBlockedBalance() {
        return blockedBalance;
    }

    public void setBlockedBalance(BigDecimal blockedBalance) {
        this.blockedBalance = blockedBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }
}