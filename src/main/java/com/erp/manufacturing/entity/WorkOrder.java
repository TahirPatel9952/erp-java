package com.erp.manufacturing.entity;

import com.erp.manufacturing.enums.WorkOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work_orders")
@SQLDelete(sql = "UPDATE work_orders SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder extends BaseEntity {

    @Column(name = "work_order_no", nullable = false, unique = true, length = 30)
    private String workOrderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false)
    private BomHeader bom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finished_goods_id", nullable = false)
    private FinishedGoods finishedGoods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "planned_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal plannedQuantity;

    @Column(name = "completed_quantity", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal completedQuantity = BigDecimal.ZERO;

    @Column(name = "rejected_quantity", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal rejectedQuantity = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private WorkOrderStatus status = WorkOrderStatus.DRAFT;

    @Column(length = 10)
    @Builder.Default
    private String priority = "NORMAL";

    @Column(name = "scheduled_start_date")
    private LocalDate scheduledStartDate;

    @Column(name = "scheduled_end_date")
    private LocalDate scheduledEndDate;

    @Column(name = "actual_start_date")
    private LocalDateTime actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDateTime actualEndDate;

    @Column(name = "batch_no", length = 50)
    private String batchNo;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("sequenceNo ASC")
    private List<ProductionStage> stages = new ArrayList<>();

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductionConsumption> consumptions = new ArrayList<>();

    @Transient
    public BigDecimal getPendingQuantity() {
        return plannedQuantity.subtract(completedQuantity).subtract(rejectedQuantity);
    }

    @Transient
    public double getCompletionPercentage() {
        if (plannedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return completedQuantity.divide(plannedQuantity, 4, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).doubleValue();
    }
}

