package com.erp.manufacturing.dto.request;

import com.erp.manufacturing.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String name;

    @Size(max = 20, message = "Category code must not exceed 20 characters")
    private String code;

    @NotNull(message = "Category type is required")
    private CategoryType type;

    private Long parentId;

    private String description;

    private Boolean isActive = true;
}

