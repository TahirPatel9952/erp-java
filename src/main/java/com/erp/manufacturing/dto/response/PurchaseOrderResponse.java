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
public class PurchaseOrderResponse {

    private Long id;
    private String poNumber;

    private Long supplierId;
    private String supplierName;
    private String supplierCode;

    private Long warehouseId;
    private String warehouseName;

    private LocalDate orderDate;
    private LocalDate expectedDate;
    private String status;

    private String paymentTerms;
    private String deliveryTerms;

    private BigDecimal subtotal;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingCharges;
    private BigDecimal grandTotal;

    private String notes;
    private String internalNotes;

    private Long approvedBy;
    private String approvedByName;
    private LocalDateTime approvedAt;

    private List<PurchaseOrderItemResponse> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}

