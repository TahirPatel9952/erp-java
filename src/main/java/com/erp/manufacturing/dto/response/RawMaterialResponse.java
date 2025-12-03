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
public class RawMaterialResponse {

    private Long id;
    private String name;
    private String code;
    private String description;
    private CategoryInfo category;
    private UnitInfo unit;
    private String hsnCode;
    private BigDecimal reorderLevel;
    private BigDecimal reorderQuantity;
    private BigDecimal minimumOrderQuantity;
    private Integer leadTimeDays;
    private BigDecimal standardCost;
    private BigDecimal lastPurchasePrice;
    private BigDecimal avgPurchasePrice;
    private BigDecimal taxPercent;
    private Integer shelfLifeDays;
    private String storageConditions;
    private Boolean isBatchTracked;
    private Boolean isActive;
    private BigDecimal totalStock;
    private BigDecimal availableStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        private Long id;
        private String name;
        private String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitInfo {
        private Long id;
        private String name;
        private String symbol;
    }
}

