package com.erp.manufacturing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bom_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_header_id", nullable = false)
    private BomHeader bomHeader;

    @Column(name = "item_type", nullable = false, length = 20)
    private String itemType; // RAW_MATERIAL, IN_PROCESS, SUB_ASSEMBLY

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "sequence_no")
    @Builder.Default
    private Integer sequenceNo = 0;

    @Column(nullable = false, precision = 15, scale = 6)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasurement unit;

    @Column(name = "wastage_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal wastagePercent = BigDecimal.ZERO;

    @Column(name = "is_critical")
    @Builder.Default
    private Boolean isCritical = false;

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
    public BigDecimal getQuantityWithWastage() {
        if (wastagePercent == null || wastagePercent.compareTo(BigDecimal.ZERO) == 0) {
            return quantity;
        }
        BigDecimal wastageMultiplier = BigDecimal.ONE.add(wastagePercent.divide(new BigDecimal("100")));
        return quantity.multiply(wastageMultiplier);
    }
}

