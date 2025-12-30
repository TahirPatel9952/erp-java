package com.erp.manufacturing.dto.response;

import com.erp.manufacturing.enums.UnitType;
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
public class UnitResponse {

    private Long id;
    private String name;
    private String symbol;
    private UnitType type;
    private Long baseUnitId;
    private String baseUnitName;
    private String baseUnitSymbol;
    private BigDecimal conversionFactor;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

