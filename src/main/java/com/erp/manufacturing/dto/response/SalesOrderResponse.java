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
public class SalesOrderResponse {

    private Long id;
    private String soNumber;

    private Long customerId;
    private String customerName;
    private String customerCode;

    private Long warehouseId;
    private String warehouseName;

    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String status;

    private String shippingAddress;
    private String paymentTerms;

    private BigDecimal subtotal;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingCharges;
    private BigDecimal grandTotal;

    private String notes;
    private String internalNotes;

    private List<SalesOrderItemResponse> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}

