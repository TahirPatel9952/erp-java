package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.request.PurchaseOrderRequest;
import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.PurchaseOrderResponse;
import com.erp.manufacturing.service.PurchaseOrderService;
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
@RequestMapping("/v1/purchase/orders")
@RequiredArgsConstructor
@Tag(name = "Purchase Orders", description = "Purchase order management APIs")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a new purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> create(@Valid @RequestBody PurchaseOrderRequest request) {
        PurchaseOrderResponse response = purchaseOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Purchase order created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                purchaseOrderService.update(id, request), "Purchase order updated successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get purchase order by ID")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get all purchase orders with pagination")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseOrderResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.getAll(pageable)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Search purchase orders")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseOrderResponse>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.search(q, PageRequest.of(page, size))));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get purchase orders by status")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseOrderResponse>>> getByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.getByStatus(status, PageRequest.of(page, size))));
    }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get purchase orders by supplier")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseOrderResponse>>> getBySupplierId(
            @PathVariable Long supplierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.getBySupplierId(supplierId, PageRequest.of(page, size))));
    }

    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get purchase orders pending approval")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponse>>> getPendingApproval() {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.getPendingApproval()));
    }

    @GetMapping("/pending-receipt")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Get purchase orders pending receipt")
    public ResponseEntity<ApiResponse<List<PurchaseOrderResponse>>> getPendingReceipt() {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.getPendingReceipt()));
    }

    @PatchMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Submit purchase order for approval")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> submit(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.submit(id), "Purchase order submitted"));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Approve purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> approve(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.approve(id), "Purchase order approved"));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Reject purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> reject(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.reject(id, reason), "Purchase order rejected"));
    }

    @PatchMapping("/{id}/send")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Send purchase order to supplier")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> sendToSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.sendToSupplier(id), "Purchase order sent to supplier"));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cancel purchase order")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> cancel(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderService.cancel(id, reason), "Purchase order cancelled"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete purchase order")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        purchaseOrderService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null, "Purchase order deleted"));
    }
}

