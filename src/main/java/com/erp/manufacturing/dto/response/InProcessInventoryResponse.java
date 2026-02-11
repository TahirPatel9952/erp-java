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
public class InProcessInventoryResponse {

    private Long id;
    private Long workOrderId;
    private String workOrderNumber;
    
    private Long finishedGoodsId;
    private String finishedGoodsCode;
    private String finishedGoodsName;
    
    private Long warehouseId;
    private String warehouseName;
    
    private String currentStage;
    private BigDecimal quantity;
    
    private Long unitId;
    private String unitName;
    private String unitSymbol;
    
    private LocalDateTime startDate;
    private LocalDateTime expectedEndDate;
    
    private String status;
    private String batchNo;
    private String notes;
}
