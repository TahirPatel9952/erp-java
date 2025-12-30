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
public class BOMItemResponse {

    private Long id;
    private String itemType;
    private Long itemId;
    private String itemCode;
    private String itemName;

    private Integer sequenceNo;
    private BigDecimal quantity;
    private BigDecimal quantityWithWastage;

    private Long unitId;
    private String unitName;
    private String unitSymbol;

    private BigDecimal wastagePercent;
    private Boolean isCritical;
    private String notes;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}

