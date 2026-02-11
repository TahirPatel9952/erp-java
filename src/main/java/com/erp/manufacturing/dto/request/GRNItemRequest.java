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
public class GRNItemRequest {

    private Long purchaseOrderItemId;

    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;

    private BigDecimal orderedQuantity;

    @NotNull(message = "Received quantity is required")
    @Positive(message = "Received quantity must be positive")
    private BigDecimal receivedQuantity;

    private BigDecimal acceptedQuantity;
    private BigDecimal rejectedQuantity;

    @NotNull(message = "Unit ID is required")
    private Long unitId;

    private BigDecimal unitPrice;
    private String batchNo;
    private String lotNo;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Long locationId;
    private String qcStatus; // PENDING, PASSED, FAILED
    private String rejectionReason;
    private String notes;
}
