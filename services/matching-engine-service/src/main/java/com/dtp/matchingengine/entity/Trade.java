package com.dtp.matchingengine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@EntityListeners(AuditingEntityListener.class)
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Orders belong to order-service.
     * Store only their identifiers; do not use cross-service JPA mappings.
     */
    @NotNull(message = "Buy order ID is required")
    @Positive(message = "Buy order ID must be greater than zero")
    @Column(name = "buy_order_id", nullable = false)
    private Long buyOrderId;

    @NotNull(message = "Sell order ID is required")
    @Positive(message = "Sell order ID must be greater than zero")
    @Column(name = "sell_order_id", nullable = false)
    private Long sellOrderId;

    /*
     * Instrument belongs to market-data-service.
     */
    @NotNull(message = "Instrument ID is required")
    @Positive(message = "Instrument ID must be greater than zero")
    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @NotNull(message = "Trade quantity is required")
    @Positive(message = "Trade quantity must be greater than zero")
    @Column(
            name = "quantity",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal quantity;

    @NotNull(message = "Trade price is required")
    @Positive(message = "Trade price must be greater than zero")
    @Column(
            name = "price",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal price;

    @Column(
            name = "total_amount",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal totalAmount;

    @CreatedDate
    @Column(
            name = "executed_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime executedAt;

    protected Trade() {
        // Required by JPA
    }

    public Trade(
            Long buyOrderId,
            Long sellOrderId,
            Long instrumentId,
            BigDecimal quantity,
            BigDecimal price
    ) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.instrumentId = instrumentId;
        this.quantity = quantity;
        this.price = price;
        calculateTotalAmount();
    }

    @PrePersist
    private void beforePersist() {
        validateOrders();
        calculateTotalAmount();
    }

    private void validateOrders() {
        if (buyOrderId != null && buyOrderId.equals(sellOrderId)) {
            throw new IllegalStateException(
                    "Buy order and sell order cannot be the same"
            );
        }
    }

    private void calculateTotalAmount() {
        if (quantity != null && price != null) {
            this.totalAmount = quantity.multiply(price);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getBuyOrderId() {
        return buyOrderId;
    }

    public Long getSellOrderId() {
        return sellOrderId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
}