package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.response.FinishedGoodsStockResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface FinishedGoodsStockService {
    
    FinishedGoodsStockResponse getById(Long id);
    
    PageResponse<FinishedGoodsStockResponse> getAll(Pageable pageable);
    
    List<FinishedGoodsStockResponse> getByFinishedGoodsId(Long finishedGoodsId);
    
    List<FinishedGoodsStockResponse> getByWarehouseId(Long warehouseId);
    
    List<FinishedGoodsStockResponse> getAllWithStock();
    
    BigDecimal getTotalStockByFinishedGoodsId(Long finishedGoodsId);
    
    BigDecimal getTotalStockByWarehouseId(Long warehouseId);
}
