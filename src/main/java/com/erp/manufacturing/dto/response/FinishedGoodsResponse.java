package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinishedGoodsResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long unitId;
    private String unitName;
    private String unitSymbol;
    private String hsnCode;
    private BigDecimal sellingPrice;
    private BigDecimal minimumSellingPrice;
    private BigDecimal mrp;
    private BigDecimal standardCost;
    private BigDecimal reorderLevel;
    private BigDecimal taxPercent;
    private Integer shelfLifeDays;
    private BigDecimal weight;
    private Long weightUnitId;
    private String weightUnitSymbol;
    private String dimensions;
    private String barcode;
    private String imageUrl;
    private Boolean isBatchTracked;
    private Boolean isActive;
    private BigDecimal currentStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

