package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.InProcessInventoryResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.service.InProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/inventory/in-process")
@RequiredArgsConstructor
@Tag(name = "In-Process Inventory", description = "In-process inventory management APIs")
public class InProcessController {

    private final InProcessService inProcessService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all in-process inventory with pagination")
    public ResponseEntity<ApiResponse<PageResponse<InProcessInventoryResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(inProcessService.getAll(pageable)));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all active in-process inventory")
    public ResponseEntity<ApiResponse<List<InProcessInventoryResponse>>> getAllActive() {
        return ResponseEntity.ok(ApiResponse.success(inProcessService.getAllActive()));
    }

    @GetMapping("/work-order/{workOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get in-process inventory by work order ID")
    public ResponseEntity<ApiResponse<InProcessInventoryResponse>> getByWorkOrderId(
            @PathVariable Long workOrderId) {
        return ResponseEntity.ok(ApiResponse.success(inProcessService.getByWorkOrderId(workOrderId)));
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get in-process inventory by warehouse ID")
    public ResponseEntity<ApiResponse<List<InProcessInventoryResponse>>> getByWarehouseId(
            @PathVariable Long warehouseId) {
        return ResponseEntity.ok(ApiResponse.success(inProcessService.getByWarehouseId(warehouseId)));
    }
}
