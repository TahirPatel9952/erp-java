package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.CustomerRequest;
import com.erp.manufacturing.dto.response.CustomerResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    
    CustomerResponse create(CustomerRequest request);
    
    CustomerResponse update(Long id, CustomerRequest request);
    
    CustomerResponse getById(Long id);
    
    CustomerResponse getByCode(String code);
    
    PageResponse<CustomerResponse> getAll(Pageable pageable);
    
    PageResponse<CustomerResponse> search(String query, Pageable pageable);
    
    List<CustomerResponse> getAllActive();
    
    List<CustomerResponse> getByType(String customerType);
    
    void delete(Long id);
    
    void activate(Long id);
    
    void deactivate(Long id);
}

