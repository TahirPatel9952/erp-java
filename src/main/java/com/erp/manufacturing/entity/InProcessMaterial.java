package com.erp.manufacturing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "in_process_materials")
@SQLDelete(sql = "UPDATE in_process_materials SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InProcessMaterial extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasurement unit;

    @Column(name = "standard_cost", precision = 15, scale = 4)
    @Builder.Default
    private java.math.BigDecimal standardCost = java.math.BigDecimal.ZERO;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
