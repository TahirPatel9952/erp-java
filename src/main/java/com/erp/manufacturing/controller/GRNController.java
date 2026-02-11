package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.GRNRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.GRNResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.service.GRNService;
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
@RequestMapping("/v1/purchase/grn")
@RequiredArgsConstructor
@Tag(name = "GRN", description = "Goods Receipt Note management APIs")
public class GRNController {

    private final GRNService grnService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Create a new GRN")
    public ResponseEntity<ApiResponse<GRNResponse>> create(@Valid @RequestBody GRNRequest request) {
        GRNResponse response = grnService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "GRN created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Update a GRN")
    public ResponseEntity<ApiResponse<GRNResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody GRNRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                grnService.update(id, request), "GRN updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get GRN by ID")
    public ResponseEntity<ApiResponse<GRNResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(grnService.getById(id)));
    }

    @GetMapping("/number/{grnNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get GRN by GRN number")
    public ResponseEntity<ApiResponse<GRNResponse>> getByGrnNumber(@PathVariable String grnNumber) {
        return ResponseEntity.ok(ApiResponse.success(grnService.getByGrnNumber(grnNumber)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all GRNs with pagination")
    public ResponseEntity<ApiResponse<PageResponse<GRNResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(grnService.getAll(pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search GRNs")
    public ResponseEntity<ApiResponse<PageResponse<GRNResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(grnService.search(q, PageRequest.of(page, size))));
    }

    @GetMapping("/purchase-order/{purchaseOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get GRNs by purchase order ID")
    public ResponseEntity<ApiResponse<List<GRNResponse>>> getByPurchaseOrderId(@PathVariable Long purchaseOrderId) {
        return ResponseEntity.ok(ApiResponse.success(grnService.getByPurchaseOrderId(purchaseOrderId)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get GRNs by status")
    public ResponseEntity<ApiResponse<List<GRNResponse>>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(ApiResponse.success(grnService.getByStatus(status)));
    }

    @GetMapping("/pending-qc")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Get GRNs pending QC")
    public ResponseEntity<ApiResponse<List<GRNResponse>>> getPendingQC() {
        return ResponseEntity.ok(ApiResponse.success(grnService.getPendingQC()));
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Verify GRN and add stock to raw materials")
    public ResponseEntity<ApiResponse<GRNResponse>> verify(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(grnService.verify(id), "GRN verified and stock added"));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cancel GRN")
    public ResponseEntity<ApiResponse<GRNResponse>> cancel(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(grnService.cancel(id, reason), "GRN cancelled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete GRN")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        grnService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "GRN deleted"));
    }
}
