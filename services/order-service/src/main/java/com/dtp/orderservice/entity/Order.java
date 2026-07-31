package com.dtp.orderservice.entity;

import com.dtp.orderservice.enums.OrderSide;
import com.dtp.orderservice.enums.OrderStatus;
import com.dtp.orderservice.enums.OrderType;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Account is owned by account-service.
     * Store only the identifier; do not create a cross-service JPA mapping.
     */
    @NotNull(message = "Account ID is required")
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    /*
     * Instrument is owned by market-data-service.
     * Store only the identifier; do not use @ManyToOne here.
     */
    @NotNull(message = "Instrument ID is required")
    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @NotNull(message = "Order type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 30)
    private OrderType orderType;

    @NotNull(message = "Order side is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "order_side", nullable = false, length = 20)
    private OrderSide orderSide;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    @Column(
            name = "quantity",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal quantity;

    /*
     * Price can be null for a MARKET order.
     * When supplied, it must be greater than zero.
     */
    @Positive(message = "Price must be greater than zero")
    @Column(
            name = "price",
            precision = 19,
            scale = 4
    )
    private BigDecimal price;

    @NotNull(message = "Filled quantity is required")
    @PositiveOrZero(message = "Filled quantity cannot be negative")
    @Column(
            name = "filled_quantity",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal filledQuantity = BigDecimal.ZERO;

    @NotNull(message = "Order status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status = OrderStatus.PENDING;

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

    protected Order() {
        // Required by JPA
    }

    public Order(
            Long accountId,
            Long instrumentId,
            OrderType orderType,
            OrderSide orderSide,
            BigDecimal quantity,
            BigDecimal price
    ) {
        this.accountId = accountId;
        this.instrumentId = instrumentId;
        this.orderType = orderType;
        this.orderSide = orderSide;
        this.quantity = quantity;
        this.price = price;
        this.filledQuantity = BigDecimal.ZERO;
        this.status = OrderStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderSide getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(OrderSide orderSide) {
        this.orderSide = orderSide;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFilledQuantity() {
        return filledQuantity;
    }

    public void setFilledQuantity(BigDecimal filledQuantity) {
        this.filledQuantity = filledQuantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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