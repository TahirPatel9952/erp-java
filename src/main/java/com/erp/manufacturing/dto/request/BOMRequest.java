package com.erp.manufacturing.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BOMRequest {

    @NotBlank(message = "BOM code is required")
    @Size(max = 30, message = "BOM code cannot exceed 30 characters")
    private String code;

    @NotNull(message = "Finished goods ID is required")
    private Long finishedGoodsId;

    @Size(max = 10, message = "Version cannot exceed 10 characters")
    private String version;

    private String description;

    private BigDecimal outputQuantity;

    private Long outputUnitId;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Integer standardTimeMinutes;

    private Integer setupTimeMinutes;

    private Boolean isActive;

    @Valid
    private List<BOMItemRequest> items;
}

