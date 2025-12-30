package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.WarehouseRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.WarehouseResponse;
import com.erp.manufacturing.service.WarehouseService;
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
@RequestMapping("/v1/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouses", description = "Warehouse management APIs")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create warehouse", description = "Create a new warehouse")
    public ResponseEntity<ApiResponse<WarehouseResponse>> create(@Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse response = warehouseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Warehouse created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update warehouse", description = "Update an existing warehouse")
    public ResponseEntity<ApiResponse<WarehouseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse response = warehouseService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Warehouse updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get warehouse by ID", description = "Get warehouse details by ID")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getById(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get warehouse by code", description = "Get warehouse details by code")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getByCode(@PathVariable String code) {
        WarehouseResponse response = warehouseService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all warehouses", description = "Get paginated list of all warehouses")
    public ResponseEntity<ApiResponse<PageResponse<WarehouseResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<WarehouseResponse> response = warehouseService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all active warehouses", description = "Get list of all active warehouses")
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> getAllActive() {
        List<WarehouseResponse> response = warehouseService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Search warehouses", description = "Search warehouses by name or code")
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> search(@RequestParam String q) {
        List<WarehouseResponse> response = warehouseService.search(q);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete warehouse", description = "Soft delete a warehouse")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        warehouseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Warehouse deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Activate warehouse", description = "Activate a warehouse")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        warehouseService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Warehouse activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('SETTINGS_MANAGE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate warehouse", description = "Deactivate a warehouse")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        warehouseService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Warehouse deactivated successfully"));
    }
}

