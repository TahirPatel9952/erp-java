package com.erp.manufacturing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemResponse {

    private Long id;
    private Long rawMaterialId;
    private String rawMaterialCode;
    private String rawMaterialName;
    private String unitName;
    private String unitSymbol;
    private BigDecimal quantity;
    private BigDecimal receivedQuantity;
    private BigDecimal unitPrice;
    private BigDecimal taxPercent;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private String notes;
}

