package com.erp.manufacturing.dto.request;

import com.erp.manufacturing.enums.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UnitRequest {

    @NotBlank(message = "Unit name is required")
    @Size(max = 50, message = "Unit name must not exceed 50 characters")
    private String name;

    @NotBlank(message = "Unit symbol is required")
    @Size(max = 10, message = "Unit symbol must not exceed 10 characters")
    private String symbol;

    @NotNull(message = "Unit type is required")
    private UnitType type;

    private Long baseUnitId;

    private BigDecimal conversionFactor = BigDecimal.ONE;

    private Boolean isActive = true;
}

