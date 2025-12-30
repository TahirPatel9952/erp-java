package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.FinishedGoodsRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.FinishedGoodsResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.service.FinishedGoodsService;
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
@RequestMapping("/v1/inventory/finished-goods")
@RequiredArgsConstructor
@Tag(name = "Finished Goods", description = "Finished goods inventory management APIs")
public class FinishedGoodsController {

    private final FinishedGoodsService finishedGoodsService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('INVENTORY_CREATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create finished goods", description = "Create a new finished goods item")
    public ResponseEntity<ApiResponse<FinishedGoodsResponse>> create(
            @Valid @RequestBody FinishedGoodsRequest request) {
        FinishedGoodsResponse response = finishedGoodsService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Finished goods created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update finished goods", description = "Update an existing finished goods item")
    public ResponseEntity<ApiResponse<FinishedGoodsResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody FinishedGoodsRequest request) {
        FinishedGoodsResponse response = finishedGoodsService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Finished goods updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get finished goods by ID", description = "Get finished goods details by ID")
    public ResponseEntity<ApiResponse<FinishedGoodsResponse>> getById(@PathVariable Long id) {
        FinishedGoodsResponse response = finishedGoodsService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get finished goods by code", description = "Get finished goods details by code")
    public ResponseEntity<ApiResponse<FinishedGoodsResponse>> getByCode(@PathVariable String code) {
        FinishedGoodsResponse response = finishedGoodsService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/barcode/{barcode}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get finished goods by barcode", description = "Get finished goods details by barcode")
    public ResponseEntity<ApiResponse<FinishedGoodsResponse>> getByBarcode(@PathVariable String barcode) {
        FinishedGoodsResponse response = finishedGoodsService.getByBarcode(barcode);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all finished goods", description = "Get paginated list of all finished goods")
    public ResponseEntity<ApiResponse<PageResponse<FinishedGoodsResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<FinishedGoodsResponse> response = finishedGoodsService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Search finished goods", description = "Search finished goods by name, code, or barcode")
    public ResponseEntity<ApiResponse<PageResponse<FinishedGoodsResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<FinishedGoodsResponse> response = finishedGoodsService.search(q, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get finished goods by category", description = "Get finished goods filtered by category")
    public ResponseEntity<ApiResponse<PageResponse<FinishedGoodsResponse>>> getByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<FinishedGoodsResponse> response = finishedGoodsService.getByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR', 'ROLE_OPERATOR', 'ROLE_VIEWER')")
    @Operation(summary = "Get all active finished goods", description = "Get list of all active finished goods")
    public ResponseEntity<ApiResponse<List<FinishedGoodsResponse>>> getAllActive() {
        List<FinishedGoodsResponse> response = finishedGoodsService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyAuthority('INVENTORY_READ', 'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_SUPERVISOR')")
    @Operation(summary = "Get low stock items", description = "Get finished goods with stock below reorder level")
    public ResponseEntity<ApiResponse<List<FinishedGoodsResponse>>> getLowStockItems() {
        List<FinishedGoodsResponse> response = finishedGoodsService.getLowStockItems();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('INVENTORY_DELETE', 'ROLE_ADMIN')")
    @Operation(summary = "Delete finished goods", description = "Soft delete a finished goods item")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        finishedGoodsService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Finished goods deleted successfully"));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('INVENTORY_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Activate finished goods", description = "Activate a finished goods item")
    public ResponseEntity<ApiResponse<Void>> activate(@PathVariable Long id) {
        finishedGoodsService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Finished goods activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('INVENTORY_UPDATE', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate finished goods", description = "Deactivate a finished goods item")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        finishedGoodsService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Finished goods deactivated successfully"));
    }
}

