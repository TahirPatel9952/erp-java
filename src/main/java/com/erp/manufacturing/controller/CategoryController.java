package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.CategoryRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.CategoryResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.enums.CategoryType;
import com.erp.manufacturing.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create category", description = "Create a new category")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Category created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Category updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get category by ID", description = "Get category details by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) {
        CategoryResponse response = categoryService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get category by code", description = "Get category details by code")
    public ResponseEntity<ApiResponse<CategoryResponse>> getByCode(@PathVariable String code) {
        CategoryResponse response = categoryService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all categories", description = "Get paginated list of all categories")
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<CategoryResponse> response = categoryService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get categories by type", description = "Get all categories of a specific type")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getByType(@PathVariable CategoryType type) {
        List<CategoryResponse> response = categoryService.getAllByType(type);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/parent/{parentId}/children")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get child categories", description = "Get child categories of a parent")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getChildren(@PathVariable Long parentId) {
        List<CategoryResponse> response = categoryService.getChildren(parentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/root")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get root categories", description = "Get all root level categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getRootCategories() {
        List<CategoryResponse> response = categoryService.getRootCategories();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get category tree", description = "Get full category tree structure")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryTree() {
        List<CategoryResponse> response = categoryService.getCategoryTree();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('SETTINGS_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all active categories", description = "Get list of all active categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllActive() {
        List<CategoryResponse> response = categoryService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete category", description = "Soft delete a category")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Activate category", description = "Activate a category")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        categoryService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate category", description = "Deactivate a category")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        categoryService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deactivated successfully"));
    }
}

