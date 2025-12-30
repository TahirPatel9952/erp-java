package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.BOMRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.BOMResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.service.BOMService;
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
@RequestMapping("/v1/production/bom")
@RequiredArgsConstructor
@Tag(name = "Bill of Materials", description = "BOM management APIs")
public class BOMController {

    private final BOMService bomService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a new BOM")
    public ResponseEntity<ApiResponse<BOMResponse>> create(@Valid @RequestBody BOMRequest request) {
        BOMResponse response = bomService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "BOM created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update an existing BOM")
    public ResponseEntity<ApiResponse<BOMResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BOMRequest request) {
        BOMResponse response = bomService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "BOM updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get BOM by ID")
    public ResponseEntity<ApiResponse<BOMResponse>> getById(@PathVariable Long id) {
        BOMResponse response = bomService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get BOM by code")
    public ResponseEntity<ApiResponse<BOMResponse>> getByCode(@PathVariable String code) {
        BOMResponse response = bomService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get all BOMs with pagination")
    public ResponseEntity<ApiResponse<PageResponse<BOMResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<BOMResponse> response = bomService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Search BOMs")
    public ResponseEntity<ApiResponse<PageResponse<BOMResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<BOMResponse> response = bomService.search(q, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get all active BOMs")
    public ResponseEntity<ApiResponse<List<BOMResponse>>> getAllActive() {
        List<BOMResponse> response = bomService.getAllActive();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/finished-goods/{finishedGoodsId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get BOMs by finished goods ID")
    public ResponseEntity<ApiResponse<List<BOMResponse>>> getByFinishedGoodsId(@PathVariable Long finishedGoodsId) {
        List<BOMResponse> response = bomService.getByFinishedGoodsId(finishedGoodsId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/finished-goods/{finishedGoodsId}/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get active BOM by finished goods ID")
    public ResponseEntity<ApiResponse<BOMResponse>> getActiveByFinishedGoodsId(@PathVariable Long finishedGoodsId) {
        BOMResponse response = bomService.getActiveByFinishedGoodsId(finishedGoodsId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Activate a BOM")
    public ResponseEntity<ApiResponse<BOMResponse>> activate(@PathVariable Long id) {
        BOMResponse response = bomService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(response, "BOM activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Deactivate a BOM")
    public ResponseEntity<ApiResponse<BOMResponse>> deactivate(@PathVariable Long id) {
        BOMResponse response = bomService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(response, "BOM deactivated successfully"));
    }

    @PostMapping("/{id}/duplicate")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Duplicate a BOM with a new version")
    public ResponseEntity<ApiResponse<BOMResponse>> duplicate(
            @PathVariable Long id,
            @RequestParam String newVersion) {
        BOMResponse response = bomService.duplicate(id, newVersion);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "BOM duplicated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a BOM")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bomService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "BOM deleted successfully"));
    }

    @GetMapping("/exists/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Check if BOM code exists")
    public ResponseEntity<ApiResponse<Boolean>> existsByCode(@PathVariable String code) {
        boolean exists = bomService.existsByCode(code);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}

