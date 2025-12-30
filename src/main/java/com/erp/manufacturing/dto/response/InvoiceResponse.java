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
public class InvoiceResponse {

    private Long id;
    private String invoiceNumber;

    private Long customerId;
    private String customerName;
    private String customerCode;

    private Long salesOrderId;
    private String salesOrderNumber;

    private Long deliveryChallanId;
    private String deliveryChallanNumber;

    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private String status;
    private String paymentStatus;

    private String billingAddress;
    private String shippingAddress;

    private BigDecimal subtotal;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingCharges;
    private BigDecimal grandTotal;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;

    private String notes;
    private String termsAndConditions;

    private List<InvoiceItemResponse> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

