package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.dto.response.FinishedGoodsStockResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.service.FinishedGoodsStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/inventory/finished-goods-stock")
@RequiredArgsConstructor
@Tag(name = "Finished Goods Stock", description = "Finished goods stock/inventory management APIs")
public class FinishedGoodsStockController {
    
    private final FinishedGoodsStockService finishedGoodsStockService;
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get finished goods stock by ID")
    public ResponseEntity<ApiResponse<FinishedGoodsStockResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(finishedGoodsStockService.getById(id)));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all finished goods stock with pagination")
    public ResponseEntity<ApiResponse<PageResponse<FinishedGoodsStockResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(finishedGoodsStockService.getAll(pageable)));
    }
    
    @GetMapping("/with-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all finished goods stock with quantity > 0")
    public ResponseEntity<ApiResponse<List<FinishedGoodsStockResponse>>> getAllWithStock() {
        return ResponseEntity.ok(ApiResponse.success(finishedGoodsStockService.getAllWithStock()));
    }
    
    @GetMapping("/finished-goods/{finishedGoodsId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get stock by finished goods ID")
    public ResponseEntity<ApiResponse<List<FinishedGoodsStockResponse>>> getByFinishedGoodsId(
            @PathVariable Long finishedGoodsId) {
        return ResponseEntity.ok(ApiResponse.success(finishedGoodsStockService.getByFinishedGoodsId(finishedGoodsId)));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get stock by warehouse ID")
    public ResponseEntity<ApiResponse<List<FinishedGoodsStockResponse>>> getByWarehouseId(
            @PathVariable Long warehouseId) {
        return ResponseEntity.ok(ApiResponse.success(finishedGoodsStockService.getByWarehouseId(warehouseId)));
    }
    
    @GetMapping("/finished-goods/{finishedGoodsId}/total")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get total stock quantity for a finished goods")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalStockByFinishedGoodsId(
            @PathVariable Long finishedGoodsId) {
        return ResponseEntity.ok(ApiResponse.success(
                finishedGoodsStockService.getTotalStockByFinishedGoodsId(finishedGoodsId)));
    }
}
