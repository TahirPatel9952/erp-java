package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderResponse {

    private Long id;
    private String workOrderNo;

    private Long bomId;
    private String bomCode;

    private Long finishedGoodsId;
    private String finishedGoodsCode;
    private String finishedGoodsName;

    private Long warehouseId;
    private String warehouseName;

    private BigDecimal plannedQuantity;
    private BigDecimal completedQuantity;
    private BigDecimal rejectedQuantity;
    private BigDecimal pendingQuantity;
    private Double completionPercentage;

    private String status;
    private String priority;

    private LocalDate scheduledStartDate;
    private LocalDate scheduledEndDate;
    private LocalDateTime actualStartDate;
    private LocalDateTime actualEndDate;

    private String batchNo;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}

