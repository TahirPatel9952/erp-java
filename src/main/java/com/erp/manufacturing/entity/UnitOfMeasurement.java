package com.erp.manufacturing.entity;

import com.erp.manufacturing.enums.UnitType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "units_of_measurement")
@SQLDelete(sql = "UPDATE units_of_measurement SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitOfMeasurement extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UnitType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_unit_id")
    private UnitOfMeasurement baseUnit;

    @Column(name = "conversion_factor", precision = 15, scale = 6)
    @Builder.Default
    private BigDecimal conversionFactor = BigDecimal.ONE;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}

