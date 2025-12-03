package com.erp.manufacturing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_consumption")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @Column(name = "item_type", nullable = false, length = 20)
    private String itemType; // RAW_MATERIAL, IN_PROCESS

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "planned_quantity", nullable = false, precision = 15, scale = 6)
    private BigDecimal plannedQuantity;

    @Column(name = "actual_quantity", precision = 15, scale = 6)
    @Builder.Default
    private BigDecimal actualQuantity = BigDecimal.ZERO;

    @Column(name = "wastage_quantity", precision = 15, scale = 6)
    @Builder.Default
    private BigDecimal wastageQuantity = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasurement unit;

    @Column(name = "batch_no", length = 50)
    private String batchNo;

    @Column(name = "consumed_at")
    private LocalDateTime consumedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Transient
    public BigDecimal getVariance() {
        return actualQuantity.subtract(plannedQuantity);
    }
}

