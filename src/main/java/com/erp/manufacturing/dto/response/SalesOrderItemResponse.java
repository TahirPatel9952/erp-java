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
public class SalesOrderItemResponse {

    private Long id;
    private Long finishedGoodsId;
    private String finishedGoodsCode;
    private String finishedGoodsName;
    private String unitName;
    private String unitSymbol;
    private BigDecimal quantity;
    private BigDecimal deliveredQuantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal taxPercent;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private String notes;
}

