package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.CategoryRequest;
import com.erp.manufacturing.dto.response.CategoryResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.enums.CategoryType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    
    CategoryResponse create(CategoryRequest request);
    
    CategoryResponse update(Long id, CategoryRequest request);
    
    CategoryResponse getById(Long id);
    
    CategoryResponse getByCode(String code);
    
    PageResponse<CategoryResponse> getAll(Pageable pageable);
    
    List<CategoryResponse> getAllByType(CategoryType type);
    
    List<CategoryResponse> getChildren(Long parentId);
    
    List<CategoryResponse> getRootCategories();
    
    List<CategoryResponse> getCategoryTree();
    
    List<CategoryResponse> getAllActive();
    
    void delete(Long id);
    
    void activate(Long id);
    
    void deactivate(Long id);
}

