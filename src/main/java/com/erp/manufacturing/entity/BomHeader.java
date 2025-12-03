package com.erp.manufacturing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bom_headers")
@SQLDelete(sql = "UPDATE bom_headers SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomHeader extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finished_goods_id", nullable = false)
    private FinishedGoods finishedGoods;

    @Column(name = "bom_code", nullable = false, unique = true, length = 30)
    private String bomCode;

    @Column(length = 10)
    @Builder.Default
    private String version = "1.0";

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "output_quantity", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal outputQuantity = BigDecimal.ONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_unit_id")
    private UnitOfMeasurement outputUnit;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "standard_time_minutes")
    @Builder.Default
    private Integer standardTimeMinutes = 0;

    @Column(name = "setup_time_minutes")
    @Builder.Default
    private Integer setupTimeMinutes = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "bomHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("sequenceNo ASC")
    private List<BomDetail> details = new ArrayList<>();

    public void addDetail(BomDetail detail) {
        details.add(detail);
        detail.setBomHeader(this);
    }

    public void removeDetail(BomDetail detail) {
        details.remove(detail);
        detail.setBomHeader(null);
    }
}

