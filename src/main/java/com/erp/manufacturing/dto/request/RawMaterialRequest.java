package com.erp.manufacturing.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    private String name;

    @NotBlank(message = "Code is required")
    @Size(max = 30, message = "Code cannot exceed 30 characters")
    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Long categoryId;

    @NotNull(message = "Unit is required")
    private Long unitId;

    @Size(max = 20, message = "HSN code cannot exceed 20 characters")
    private String hsnCode;

    @DecimalMin(value = "0", message = "Reorder level must be non-negative")
    private BigDecimal reorderLevel;

    @DecimalMin(value = "0", message = "Reorder quantity must be non-negative")
    private BigDecimal reorderQuantity;

    @DecimalMin(value = "0", message = "Minimum order quantity must be non-negative")
    private BigDecimal minimumOrderQuantity;

    @Min(value = 0, message = "Lead time days must be non-negative")
    private Integer leadTimeDays;

    @DecimalMin(value = "0", message = "Standard cost must be non-negative")
    private BigDecimal standardCost;

    @DecimalMin(value = "0", inclusive = false, message = "Tax percent must be positive")
    @DecimalMax(value = "100", message = "Tax percent cannot exceed 100")
    private BigDecimal taxPercent;

    @Min(value = 0, message = "Shelf life days must be non-negative")
    private Integer shelfLifeDays;

    @Size(max = 255, message = "Storage conditions cannot exceed 255 characters")
    private String storageConditions;

    private Boolean isBatchTracked;

    private Boolean isActive;
}

