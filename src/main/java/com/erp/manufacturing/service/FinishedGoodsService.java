package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.FinishedGoodsRequest;
import com.erp.manufacturing.dto.response.FinishedGoodsResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinishedGoodsService {
    
    FinishedGoodsResponse create(FinishedGoodsRequest request);
    
    FinishedGoodsResponse update(Long id, FinishedGoodsRequest request);
    
    FinishedGoodsResponse getById(Long id);
    
    FinishedGoodsResponse getByCode(String code);
    
    FinishedGoodsResponse getByBarcode(String barcode);
    
    PageResponse<FinishedGoodsResponse> getAll(Pageable pageable);
    
    PageResponse<FinishedGoodsResponse> search(String query, Pageable pageable);
    
    PageResponse<FinishedGoodsResponse> getByCategory(Long categoryId, Pageable pageable);
    
    List<FinishedGoodsResponse> getAllActive();
    
    List<FinishedGoodsResponse> getLowStockItems();
    
    void delete(Long id);
    
    void activate(Long id);
    
    void deactivate(Long id);
}

