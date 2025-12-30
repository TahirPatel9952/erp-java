package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.SupplierRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.SupplierResponse;
import com.erp.manufacturing.service.SupplierService;
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
@RequestMapping("/v1/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Supplier management APIs")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('PURCHASE_CREATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create supplier", description = "Create a new supplier")
    public ResponseEntity<ApiResponse<SupplierResponse>> create(@Valid @RequestBody SupplierRequest request) {
        SupplierResponse response = supplierService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Supplier created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PURCHASE_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update supplier", description = "Update an existing supplier")
    public ResponseEntity<ApiResponse<SupplierResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request) {
        SupplierResponse response = supplierService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Supplier updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PURCHASE_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get supplier by ID", description = "Get supplier details by ID")
    public ResponseEntity<ApiResponse<SupplierResponse>> getById(@PathVariable Long id) {
        SupplierResponse response = supplierService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('PURCHASE_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get supplier by code", description = "Get supplier details by code")
    public ResponseEntity<ApiResponse<SupplierResponse>> getByCode(@PathVariable String code) {
        SupplierResponse response = supplierService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('PURCHASE_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all suppliers", description = "Get paginated list of all suppliers")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<SupplierResponse> response = supplierService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('PURCHASE_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Search suppliers", description = "Search suppliers by name, code, contact, or email")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<SupplierResponse> response = supplierService.search(q, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('PURCHASE_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all active suppliers", description = "Get list of all active suppliers")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllActive() {
        List<SupplierResponse> response = supplierService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PURCHASE_DELETE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete supplier", description = "Soft delete a supplier")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('PURCHASE_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Activate supplier", description = "Activate a supplier")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        supplierService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('PURCHASE_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate supplier", description = "Deactivate a supplier")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        supplierService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Supplier deactivated successfully"));
    }
}

