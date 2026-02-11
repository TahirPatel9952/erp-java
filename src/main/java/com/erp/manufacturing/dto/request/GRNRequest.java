package com.erp.manufacturing.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GRNRequest {

    private Long purchaseOrderId;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotNull(message = "Receipt date is required")
    private LocalDate receiptDate;

    private String vehicleNo;
    private String driverName;
    private String challanNo;
    private LocalDate challanDate;
    private String notes;

    @Valid
    @NotNull(message = "Items are required")
    private List<GRNItemRequest> items;
}
