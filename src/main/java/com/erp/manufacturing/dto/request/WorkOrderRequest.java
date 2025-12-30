package com.erp.manufacturing.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderRequest {

    @NotNull(message = "BOM ID is required")
    private Long bomId;

    @NotNull(message = "Finished goods ID is required")
    private Long finishedGoodsId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Planned quantity is required")
    @Positive(message = "Planned quantity must be positive")
    private BigDecimal plannedQuantity;

    private String priority;
    private LocalDate scheduledStartDate;
    private LocalDate scheduledEndDate;
    private String batchNo;
    private String notes;
}

