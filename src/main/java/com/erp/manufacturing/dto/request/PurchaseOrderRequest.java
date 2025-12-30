package com.erp.manufacturing.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderRequest {

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    private LocalDate orderDate;
    private LocalDate expectedDate;
    private String paymentTerms;
    private String deliveryTerms;
    private BigDecimal discountPercent;
    private BigDecimal shippingCharges;
    private String notes;
    private String internalNotes;

    @Valid
    private List<PurchaseOrderItemRequest> items;
}

