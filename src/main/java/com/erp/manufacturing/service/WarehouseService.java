package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.WarehouseRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.WarehouseResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarehouseService {
    
    WarehouseResponse create(WarehouseRequest request);
    
    WarehouseResponse update(Long id, WarehouseRequest request);
    
    WarehouseResponse getById(Long id);
    
    WarehouseResponse getByCode(String code);
    
    PageResponse<WarehouseResponse> getAll(Pageable pageable);
    
    List<WarehouseResponse> getAllActive();
    
    List<WarehouseResponse> search(String query);
    
    void delete(Long id);
    
    void activate(Long id);
    
    void deactivate(Long id);
}

