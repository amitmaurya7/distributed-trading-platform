package com.dtp.portfolioservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "holdings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_holdings_account_instrument",
                        columnNames = {"account_id", "instrument_id"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Holding {

    private static final int MONEY_SCALE = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Account belongs to account-service.
     * Store only its identifier.
     */
    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be greater than zero")
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    /*
     * Instrument belongs to market-data-service.
     * Store only its identifier.
     */
    @NotNull(message = "Instrument ID is required")
    @Positive(message = "Instrument ID must be greater than zero")
    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    @Column(
            name = "quantity",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal quantity = BigDecimal.ZERO;

    @NotNull(message = "Average price is required")
    @PositiveOrZero(message = "Average price cannot be negative")
    @Column(
            name = "average_price",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal averagePrice = BigDecimal.ZERO;

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @Version
    @Column(
            name = "version",
            nullable = false
    )
    private Long version;

    protected Holding() {
        // Required by JPA
    }

    public Holding(Long accountId, Long instrumentId) {
        this.accountId = accountId;
        this.instrumentId = instrumentId;
        this.quantity = BigDecimal.ZERO;
        this.averagePrice = BigDecimal.ZERO;
    }

    public Holding(
            Long accountId,
            Long instrumentId,
            BigDecimal quantity,
            BigDecimal averagePrice
    ) {
        validateNonNegative(quantity, "Quantity");
        validateNonNegative(averagePrice, "Average price");

        this.accountId = accountId;
        this.instrumentId = instrumentId;
        this.quantity = scale(quantity);
        this.averagePrice = scale(averagePrice);
    }

    /**
     * Adds newly purchased units and recalculates the weighted average price.
     */
    public void addQuantity(
            BigDecimal purchasedQuantity,
            BigDecimal purchasePrice
    ) {
        validatePositive(purchasedQuantity, "Purchased quantity");
        validatePositive(purchasePrice, "Purchase price");

        BigDecimal existingValue =
                quantity.multiply(averagePrice);

        BigDecimal purchasedValue =
                purchasedQuantity.multiply(purchasePrice);

        BigDecimal newQuantity =
                quantity.add(purchasedQuantity);

        this.averagePrice = existingValue
                .add(purchasedValue)
                .divide(newQuantity, MONEY_SCALE, RoundingMode.HALF_UP);

        this.quantity = scale(newQuantity);
    }

    /**
     * Removes sold units. Average acquisition price remains unchanged
     * while some quantity is still held.
     */
    public void removeQuantity(BigDecimal soldQuantity) {
        validatePositive(soldQuantity, "Sold quantity");

        if (soldQuantity.compareTo(quantity) > 0) {
            throw new IllegalStateException(
                    "Sold quantity cannot exceed available holding quantity"
            );
        }

        this.quantity = scale(quantity.subtract(soldQuantity));

        if (this.quantity.compareTo(BigDecimal.ZERO) == 0) {
            this.averagePrice = BigDecimal.ZERO.setScale(
                    MONEY_SCALE,
                    RoundingMode.HALF_UP
            );
        }
    }

    private static void validatePositive(
            BigDecimal value,
            String fieldName
    ) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero"
            );
        }
    }

    private static void validateNonNegative(
            BigDecimal value,
            String fieldName
    ) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be negative"
            );
        }
    }

    private static BigDecimal scale(BigDecimal value) {
        return value.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
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