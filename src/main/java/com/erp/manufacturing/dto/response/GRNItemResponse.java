package com.erp.manufacturing.dto.response;

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
public class GRNItemResponse {

    private Long id;
    private Long purchaseOrderItemId;
    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private Long unitId;
    private String unitName;
    private String unitSymbol;
    private BigDecimal orderedQuantity;
    private BigDecimal receivedQuantity;
    private BigDecimal acceptedQuantity;
    private BigDecimal rejectedQuantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String batchNo;
    private String lotNo;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private Long locationId;
    private String locationCode;
    private String qcStatus; // PENDING, PASSED, FAILED
    private String rejectionReason;
    private String notes;
}
