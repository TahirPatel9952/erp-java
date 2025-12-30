package com.erp.manufacturing.service;

import com.erp.manufacturing.dto.request.BOMRequest;
import com.erp.manufacturing.dto.response.BOMResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BOMService {

    BOMResponse create(BOMRequest request);

    BOMResponse update(Long id, BOMRequest request);

    BOMResponse getById(Long id);

    BOMResponse getByCode(String code);

    PageResponse<BOMResponse> getAll(Pageable pageable);

    PageResponse<BOMResponse> search(String searchTerm, Pageable pageable);

    List<BOMResponse> getAllActive();

    List<BOMResponse> getByFinishedGoodsId(Long finishedGoodsId);

    BOMResponse getActiveByFinishedGoodsId(Long finishedGoodsId);

    BOMResponse activate(Long id);

    BOMResponse deactivate(Long id);

    BOMResponse duplicate(Long id, String newVersion);

    void delete(Long id);

    boolean existsByCode(String code);
}

