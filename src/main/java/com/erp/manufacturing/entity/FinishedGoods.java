package com.erp.manufacturing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "finished_goods")
@SQLDelete(sql = "UPDATE finished_goods SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinishedGoods extends BaseEntity {

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

    @Column(name = "selling_price", nullable = false, precision = 15, scale = 4)
    private BigDecimal sellingPrice;

    @Column(name = "minimum_selling_price", precision = 15, scale = 4)
    private BigDecimal minimumSellingPrice;

    @Column(precision = 15, scale = 4)
    private BigDecimal mrp;

    @Column(name = "standard_cost", precision = 15, scale = 4)
    @Builder.Default
    private BigDecimal standardCost = BigDecimal.ZERO;

    @Column(name = "reorder_level", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal reorderLevel = BigDecimal.ZERO;

    @Column(name = "tax_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxPercent = new BigDecimal("18");

    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;

    @Column(precision = 10, scale = 4)
    private BigDecimal weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weight_unit_id")
    private UnitOfMeasurement weightUnit;

    @Column(length = 50)
    private String dimensions;

    @Column(unique = true, length = 50)
    private String barcode;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_batch_tracked")
    @Builder.Default
    private Boolean isBatchTracked = true;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}

