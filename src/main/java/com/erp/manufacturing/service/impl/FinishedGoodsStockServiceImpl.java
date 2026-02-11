package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.response.FinishedGoodsStockResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.entity.FinishedGoodsStock;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.FinishedGoodsStockRepository;
import com.erp.manufacturing.service.FinishedGoodsStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FinishedGoodsStockServiceImpl implements FinishedGoodsStockService {
    
    private final FinishedGoodsStockRepository finishedGoodsStockRepository;
    
    @Override
    public FinishedGoodsStockResponse getById(Long id) {
        FinishedGoodsStock stock = finishedGoodsStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods Stock", "id", id));
        return mapToResponse(stock);
    }
    
    @Override
    public PageResponse<FinishedGoodsStockResponse> getAll(Pageable pageable) {
        Page<FinishedGoodsStock> page = finishedGoodsStockRepository.findAll(pageable);
        List<FinishedGoodsStockResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }
    
    @Override
    public List<FinishedGoodsStockResponse> getByFinishedGoodsId(Long finishedGoodsId) {
        return finishedGoodsStockRepository.findByFinishedGoodsId(finishedGoodsId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FinishedGoodsStockResponse> getByWarehouseId(Long warehouseId) {
        return finishedGoodsStockRepository.findByWarehouseId(warehouseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FinishedGoodsStockResponse> getAllWithStock() {
        return finishedGoodsStockRepository.findAllWithStock().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public BigDecimal getTotalStockByFinishedGoodsId(Long finishedGoodsId) {
        BigDecimal total = finishedGoodsStockRepository.getTotalQuantityByFinishedGoodsId(finishedGoodsId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getTotalStockByWarehouseId(Long warehouseId) {
        BigDecimal total = finishedGoodsStockRepository.getTotalQuantityByWarehouseId(warehouseId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    private FinishedGoodsStockResponse mapToResponse(FinishedGoodsStock stock) {
        BigDecimal totalValue = stock.getQuantity().multiply(
                stock.getUnitCost() != null ? stock.getUnitCost() : BigDecimal.ZERO);
        
        return FinishedGoodsStockResponse.builder()
                .id(stock.getId())
                .finishedGoodsId(stock.getFinishedGoods().getId())
                .finishedGoodsCode(stock.getFinishedGoods().getCode())
                .finishedGoodsName(stock.getFinishedGoods().getName())
                .warehouseId(stock.getWarehouse().getId())
                .warehouseName(stock.getWarehouse().getName())
                .locationId(stock.getLocation() != null ? stock.getLocation().getId() : null)
                .locationName(stock.getLocation() != null ? stock.getLocation().getLocationCode() : null)
                .quantity(stock.getQuantity())
                .reservedQuantity(stock.getReservedQuantity())
                .availableQuantity(stock.getAvailableQuantity())
                .batchNo(stock.getBatchNo())
                .lotNo(stock.getLotNo())
                .manufacturingDate(stock.getManufacturingDate())
                .expiryDate(stock.getExpiryDate())
                .unitCost(stock.getUnitCost())
                .totalValue(totalValue)
                .createdAt(stock.getCreatedAt())
                .updatedAt(stock.getUpdatedAt())
                .build();
    }
}
