package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.UnitRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.UnitResponse;
import com.erp.manufacturing.enums.UnitType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnitService {
    
    UnitResponse create(UnitRequest request);
    
    UnitResponse update(Long id, UnitRequest request);
    
    UnitResponse getById(Long id);
    
    UnitResponse getBySymbol(String symbol);
    
    PageResponse<UnitResponse> getAll(Pageable pageable);
    
    List<UnitResponse> getAllByType(UnitType type);
    
    List<UnitResponse> getAllActive();
    
    List<UnitResponse> getBaseUnits();
    
    void delete(Long id);
    
    void activate(Long id);
    
    void deactivate(Long id);
}

