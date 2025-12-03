package com.erp.manufacturing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "raw_materials")
@SQLDelete(sql = "UPDATE raw_materials SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterial extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasurement unit;

    @Column(name = "hsn_code", length = 20)
    private String hsnCode;

    @Column(name = "reorder_level", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal reorderLevel = BigDecimal.ZERO;

    @Column(name = "reorder_quantity", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal reorderQuantity = BigDecimal.ZERO;

    @Column(name = "minimum_order_quantity", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal minimumOrderQuantity = BigDecimal.ONE;

    @Column(name = "lead_time_days")
    @Builder.Default
    private Integer leadTimeDays = 0;

    @Column(name = "standard_cost", precision = 15, scale = 4)
    @Builder.Default
    private BigDecimal standardCost = BigDecimal.ZERO;

    @Column(name = "last_purchase_price", precision = 15, scale = 4)
    @Builder.Default
    private BigDecimal lastPurchasePrice = BigDecimal.ZERO;

    @Column(name = "avg_purchase_price", precision = 15, scale = 4)
    @Builder.Default
    private BigDecimal avgPurchasePrice = BigDecimal.ZERO;

    @Column(name = "tax_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxPercent = new BigDecimal("18");

    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;

    @Column(name = "storage_conditions", length = 255)
    private String storageConditions;

    @Column(name = "is_batch_tracked")
    @Builder.Default
    private Boolean isBatchTracked = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}

