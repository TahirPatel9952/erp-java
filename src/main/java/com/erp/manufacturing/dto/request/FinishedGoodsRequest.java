package com.erp.manufacturing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinishedGoodsRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Product code is required")
    @Size(max = 30, message = "Product code must not exceed 30 characters")
    private String code;

    private String description;

    private Long categoryId;

    @NotNull(message = "Unit is required")
    private Long unitId;

    @Size(max = 20, message = "HSN code must not exceed 20 characters")
    private String hsnCode;

    @NotNull(message = "Selling price is required")
    @Positive(message = "Selling price must be positive")
    private BigDecimal sellingPrice;

    private BigDecimal minimumSellingPrice;

    private BigDecimal mrp;

    private BigDecimal standardCost;

    private BigDecimal reorderLevel;

    private BigDecimal taxPercent;

    private Integer shelfLifeDays;

    private BigDecimal weight;

    private Long weightUnitId;

    @Size(max = 50, message = "Dimensions must not exceed 50 characters")
    private String dimensions;

    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    private String barcode;

    private String imageUrl;

    private Boolean isBatchTracked = true;

    private Boolean isActive = true;
}

