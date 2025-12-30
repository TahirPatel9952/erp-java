package com.erp.manufacturing.controller;

import com.erp.manufacturing.dto.response.ApiResponse;
import com.erp.manufacturing.repository.RawMaterialStockRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/inventory/stock")
@RequiredArgsConstructor
@Tag(name = "Stock", description = "Stock management APIs")
public class StockController {

    private final RawMaterialStockRepository rawMaterialStockRepository;

    @GetMapping("/raw-materials")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get raw material stock levels")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRawMaterialStock() {
        List<Map<String, Object>> stocks = rawMaterialStockRepository.findAll().stream()
                .map(stock -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", stock.getId());
                    map.put("rawMaterialId", stock.getRawMaterial().getId());
                    map.put("rawMaterialCode", stock.getRawMaterial().getCode());
                    map.put("rawMaterialName", stock.getRawMaterial().getName());
                    map.put("warehouseId", stock.getWarehouse().getId());
                    map.put("warehouseName", stock.getWarehouse().getName());
                    map.put("quantity", stock.getQuantity());
                    map.put("reservedQuantity", stock.getReservedQuantity());
                    map.put("availableQuantity", stock.getAvailableQuantity());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(stocks));
    }

    @GetMapping("/raw-materials/{rawMaterialId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get stock for specific raw material")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStockByRawMaterial(@PathVariable Long rawMaterialId) {
        List<Map<String, Object>> stocks = rawMaterialStockRepository.findByRawMaterialId(rawMaterialId).stream()
                .map(stock -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", stock.getId());
                    map.put("warehouseId", stock.getWarehouse().getId());
                    map.put("warehouseName", stock.getWarehouse().getName());
                    map.put("quantity", stock.getQuantity());
                    map.put("reservedQuantity", stock.getReservedQuantity());
                    map.put("availableQuantity", stock.getAvailableQuantity());
                    map.put("batchNo", stock.getBatchNo());
                    map.put("expiryDate", stock.getExpiryDate());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(stocks));
    }

    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR', 'OPERATOR')")
    @Operation(summary = "Get all stock in a warehouse")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStockByWarehouse(@PathVariable Long warehouseId) {
        List<Map<String, Object>> stocks = rawMaterialStockRepository.findByWarehouseId(warehouseId).stream()
                .map(stock -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", stock.getId());
                    map.put("rawMaterialId", stock.getRawMaterial().getId());
                    map.put("rawMaterialCode", stock.getRawMaterial().getCode());
                    map.put("rawMaterialName", stock.getRawMaterial().getName());
                    map.put("quantity", stock.getQuantity());
                    map.put("reservedQuantity", stock.getReservedQuantity());
                    map.put("availableQuantity", stock.getAvailableQuantity());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(stocks));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SUPERVISOR')")
    @Operation(summary = "Get items below reorder level")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getLowStock() {
        List<Map<String, Object>> items = rawMaterialStockRepository.findLowStockItems().stream()
                .map(stock -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", stock.getId());
                    map.put("rawMaterialId", stock.getRawMaterial().getId());
                    map.put("rawMaterialCode", stock.getRawMaterial().getCode());
                    map.put("rawMaterialName", stock.getRawMaterial().getName());
                    map.put("currentStock", stock.getQuantity());
                    map.put("reorderLevel", stock.getRawMaterial().getReorderLevel());
                    map.put("unitName", stock.getRawMaterial().getUnit() != null ? 
                            stock.getRawMaterial().getUnit().getSymbol() : "");
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(items));
    }
}

