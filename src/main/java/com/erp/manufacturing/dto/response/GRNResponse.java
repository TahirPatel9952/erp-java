package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GRNResponse {

    private Long id;
    private String grnNumber;
    private Long purchaseOrderId;
    private String purchaseOrderNumber;
    private Long supplierId;
    private String supplierName;
    private String supplierCode;
    private Long warehouseId;
    private String warehouseName;
    private LocalDate receiptDate;
    private String status; // DRAFT, PENDING_QC, QC_IN_PROGRESS, QC_COMPLETED, VERIFIED, CANCELLED
    private String vehicleNo;
    private String driverName;
    private String challanNo;
    private LocalDate challanDate;
    private String notes;
    private String qcNotes;
    private Long qcBy;
    private String qcByName;
    private LocalDateTime qcAt;
    private Integer totalItems;
    private BigDecimal totalQuantity;
    private BigDecimal acceptedQuantity;
    private BigDecimal rejectedQuantity;
    private BigDecimal totalAmount;
    private List<GRNItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}
