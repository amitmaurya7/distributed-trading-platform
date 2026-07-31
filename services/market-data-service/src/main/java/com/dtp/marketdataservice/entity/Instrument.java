package com.dtp.marketdataservice.entity;

import com.dtp.marketdataservice.enums.InstrumentStatus;
import com.dtp.marketdataservice.enums.InstrumentType;
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
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "instruments")
@EntityListeners(AuditingEntityListener.class)
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Instrument symbol is required")
    @Size(max = 30, message = "Instrument symbol cannot exceed 30 characters")
    @Column(
            name = "symbol",
            nullable = false,
            unique = true,
            length = 30
    )
    private String symbol;

    @NotBlank(message = "Instrument name is required")
    @Size(max = 150, message = "Instrument name cannot exceed 150 characters")
    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    private String name;

    @NotNull(message = "Instrument type is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "instrument_type",
            nullable = false,
            length = 30
    )
    private InstrumentType instrumentType;

    /*
     * The database permits current_price to be null.
     * Null means the latest market price is not yet available.
     */
    @Positive(message = "Current price must be greater than zero")
    @Column(
            name = "current_price",
            precision = 19,
            scale = 4
    )
    private BigDecimal currentPrice;

    @NotNull(message = "Instrument status is required")
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private InstrumentStatus status = InstrumentStatus.ACTIVE;

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

    protected Instrument() {
        // Required by JPA
    }

    public Instrument(
            String symbol,
            String name,
            InstrumentType instrumentType
    ) {
        this.symbol = symbol;
        this.name = name;
        this.instrumentType = instrumentType;
        this.status = InstrumentStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public InstrumentStatus getStatus() {
        return status;
    }

    public void setStatus(InstrumentStatus status) {
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