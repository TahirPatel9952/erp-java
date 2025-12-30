package com.erp.manufacturing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BOMItemRequest {

    private Long id; // For updates

    @NotBlank(message = "Item type is required")
    private String itemType; // RAW_MATERIAL, IN_PROCESS, SUB_ASSEMBLY

    @NotNull(message = "Item ID is required")
    private Long itemId;

    private Integer sequenceNo;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @NotNull(message = "Unit ID is required")
    private Long unitId;

    private BigDecimal wastagePercent;

    private Boolean isCritical;

    private String notes;
}

