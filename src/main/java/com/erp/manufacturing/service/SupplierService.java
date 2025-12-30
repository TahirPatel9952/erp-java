package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.SupplierRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.SupplierResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {
    
    SupplierResponse create(SupplierRequest request);
    
    SupplierResponse update(Long id, SupplierRequest request);
    
    SupplierResponse getById(Long id);
    
    SupplierResponse getByCode(String code);
    
    PageResponse<SupplierResponse> getAll(Pageable pageable);
    
    PageResponse<SupplierResponse> search(String query, Pageable pageable);
    
    List<SupplierResponse> getAllActive();
    
    void delete(Long id);
    
    void activate(Long id);
    
    void deactivate(Long id);
}

