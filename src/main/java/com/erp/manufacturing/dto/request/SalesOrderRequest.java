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
public class SalesOrderRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Long warehouseId;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String shippingAddress;
    private String paymentTerms;
    private BigDecimal discountPercent;
    private BigDecimal shippingCharges;
    private String notes;
    private String internalNotes;

    @Valid
    private List<SalesOrderItemRequest> items;
}

