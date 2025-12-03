package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.RawMaterialRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.RawMaterialResponse;
import com.erp.manufacturing.service.RawMaterialService;
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
@RequestMapping("/v1/inventory/raw-materials")
@RequiredArgsConstructor
@Tag(name = "Raw Materials", description = "Raw materials inventory management APIs")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('INVENTORY_CREATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create raw material", description = "Create a new raw material item")
    public ResponseEntity<ApiResponse<RawMaterialResponse>> create(
            @Valid @RequestBody RawMaterialRequest request) {
        RawMaterialResponse response = rawMaterialService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Raw material created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update raw material", description = "Update an existing raw material item")
    public ResponseEntity<ApiResponse<RawMaterialResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody RawMaterialRequest request) {
        RawMaterialResponse response = rawMaterialService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Raw material updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get raw material by ID", description = "Get raw material details by ID")
    public ResponseEntity<ApiResponse<RawMaterialResponse>> getById(@PathVariable Long id) {
        RawMaterialResponse response = rawMaterialService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get raw material by code", description = "Get raw material details by code")
    public ResponseEntity<ApiResponse<RawMaterialResponse>> getByCode(@PathVariable String code) {
        RawMaterialResponse response = rawMaterialService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all raw materials", description = "Get paginated list of all raw materials")
    public ResponseEntity<ApiResponse<PageResponse<RawMaterialResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<RawMaterialResponse> response = rawMaterialService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Search raw materials", description = "Search raw materials by name, code, or description")
    public ResponseEntity<ApiResponse<PageResponse<RawMaterialResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<RawMaterialResponse> response = rawMaterialService.search(q, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get raw materials by category", description = "Get raw materials filtered by category")
    public ResponseEntity<ApiResponse<PageResponse<RawMaterialResponse>>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<RawMaterialResponse> response = rawMaterialService.getByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all active raw materials", description = "Get list of all active raw materials")
    public ResponseEntity<ApiResponse<List<RawMaterialResponse>>> getAllActive() {
        List<RawMaterialResponse> response = rawMaterialService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR')")
    @Operation(summary = "Get low stock items", description = "Get raw materials with stock below reorder level")
    public ResponseEntity<ApiResponse<List<RawMaterialResponse>>> getLowStockItems() {
        List<RawMaterialResponse> response = rawMaterialService.getLowStockItems();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_DELETE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete raw material", description = "Soft delete a raw material item")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rawMaterialService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Raw material deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('INVENTORY_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Activate raw material", description = "Activate a raw material item")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        rawMaterialService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Raw material activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('INVENTORY_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate raw material", description = "Deactivate a raw material item")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        rawMaterialService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Raw material deactivated successfully"));
    }
}

