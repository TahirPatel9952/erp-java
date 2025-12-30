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
public class BOMResponse {

    private Long id;
    private String code;
    private String version;
    private String description;

    private Long finishedGoodsId;
    private String finishedGoodsCode;
    private String finishedGoodsName;

    private BigDecimal outputQuantity;
    private Long outputUnitId;
    private String outputUnitName;
    private String outputUnitSymbol;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private Integer standardTimeMinutes;
    private Integer setupTimeMinutes;

    private Boolean isActive;
    private String status;

    private BigDecimal totalMaterialCost;
    private BigDecimal laborCost;
    private BigDecimal overheadCost;
    private BigDecimal totalCost;

    private List<BOMItemResponse> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
}

