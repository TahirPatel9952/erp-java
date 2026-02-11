package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinishedGoodsStockResponse {
    
    private Long id;
    
    private Long finishedGoodsId;
    private String finishedGoodsCode;
    private String finishedGoodsName;
    
    private Long warehouseId;
    private String warehouseName;
    
    private Long locationId;
    private String locationName;
    
    private BigDecimal quantity;
    private BigDecimal reservedQuantity;
    private BigDecimal availableQuantity;
    
    private String batchNo;
    private String lotNo;
    
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    
    private BigDecimal unitCost;
    private BigDecimal totalValue;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
