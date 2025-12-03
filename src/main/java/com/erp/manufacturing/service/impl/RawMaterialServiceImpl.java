package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.RawMaterialRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.RawMaterialResponse;
import com.erp.manufacturing.entity.Category;
import com.erp.manufacturing.entity.RawMaterial;
import com.erp.manufacturing.entity.UnitOfMeasurement;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.CategoryRepository;
import com.erp.manufacturing.repository.RawMaterialRepository;
import com.erp.manufacturing.repository.RawMaterialStockRepository;
import com.erp.manufacturing.repository.UnitOfMeasurementRepository;
import com.erp.manufacturing.service.RawMaterialService;
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
public class RawMaterialServiceImpl implements RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialStockRepository stockRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasurementRepository unitRepository;

    @Override
    @Transactional
    public RawMaterialResponse create(RawMaterialRequest request) {
        if (rawMaterialRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Raw Material", "code", request.getCode());
        }

        UnitOfMeasurement unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getUnitId()));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        }

        RawMaterial rawMaterial = RawMaterial.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .category(category)
                .unit(unit)
                .hsnCode(request.getHsnCode())
                .reorderLevel(request.getReorderLevel() != null ? request.getReorderLevel() : BigDecimal.ZERO)
                .reorderQuantity(request.getReorderQuantity() != null ? request.getReorderQuantity() : BigDecimal.ZERO)
                .minimumOrderQuantity(request.getMinimumOrderQuantity() != null ? request.getMinimumOrderQuantity() : BigDecimal.ONE)
                .leadTimeDays(request.getLeadTimeDays() != null ? request.getLeadTimeDays() : 0)
                .standardCost(request.getStandardCost() != null ? request.getStandardCost() : BigDecimal.ZERO)
                .taxPercent(request.getTaxPercent() != null ? request.getTaxPercent() : new BigDecimal("18"))
                .shelfLifeDays(request.getShelfLifeDays())
                .storageConditions(request.getStorageConditions())
                .isBatchTracked(request.getIsBatchTracked() != null ? request.getIsBatchTracked() : false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        RawMaterial saved = rawMaterialRepository.save(rawMaterial);
        log.info("Raw material created: {}", saved.getCode());

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public RawMaterialResponse update(Long id, RawMaterialRequest request) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "id", id));

        if (!rawMaterial.getCode().equals(request.getCode()) && 
            rawMaterialRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Raw Material", "code", request.getCode());
        }

        UnitOfMeasurement unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getUnitId()));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        }

        rawMaterial.setName(request.getName());
        rawMaterial.setCode(request.getCode());
        rawMaterial.setDescription(request.getDescription());
        rawMaterial.setCategory(category);
        rawMaterial.setUnit(unit);
        rawMaterial.setHsnCode(request.getHsnCode());
        rawMaterial.setReorderLevel(request.getReorderLevel());
        rawMaterial.setReorderQuantity(request.getReorderQuantity());
        rawMaterial.setMinimumOrderQuantity(request.getMinimumOrderQuantity());
        rawMaterial.setLeadTimeDays(request.getLeadTimeDays());
        rawMaterial.setStandardCost(request.getStandardCost());
        rawMaterial.setTaxPercent(request.getTaxPercent());
        rawMaterial.setShelfLifeDays(request.getShelfLifeDays());
        rawMaterial.setStorageConditions(request.getStorageConditions());
        rawMaterial.setIsBatchTracked(request.getIsBatchTracked());
        
        if (request.getIsActive() != null) {
            rawMaterial.setIsActive(request.getIsActive());
        }

        RawMaterial saved = rawMaterialRepository.save(rawMaterial);
        log.info("Raw material updated: {}", saved.getCode());

        return mapToResponse(saved);
    }

    @Override
    public RawMaterialResponse getById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "id", id));
        return mapToResponse(rawMaterial);
    }

    @Override
    public RawMaterialResponse getByCode(String code) {
        RawMaterial rawMaterial = rawMaterialRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "code", code));
        return mapToResponse(rawMaterial);
    }

    @Override
    public PageResponse<RawMaterialResponse> getAll(Pageable pageable) {
        Page<RawMaterial> page = rawMaterialRepository.findAll(pageable);
        return PageResponse.from(page, page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    @Override
    public PageResponse<RawMaterialResponse> search(String search, Pageable pageable) {
        Page<RawMaterial> page = rawMaterialRepository.search(search, pageable);
        return PageResponse.from(page, page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    @Override
    public PageResponse<RawMaterialResponse> getByCategory(Long categoryId, Pageable pageable) {
        Page<RawMaterial> page = rawMaterialRepository.findByCategoryId(categoryId, pageable);
        return PageResponse.from(page, page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    @Override
    public List<RawMaterialResponse> getAllActive() {
        return rawMaterialRepository.findAllActive().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RawMaterialResponse> getLowStockItems() {
        return rawMaterialRepository.findLowStockItems().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "id", id));
        rawMaterialRepository.delete(rawMaterial);
        log.info("Raw material deleted: {}", rawMaterial.getCode());
    }

    @Override
    @Transactional
    public void activate(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "id", id));
        rawMaterial.setIsActive(true);
        rawMaterialRepository.save(rawMaterial);
        log.info("Raw material activated: {}", rawMaterial.getCode());
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "id", id));
        rawMaterial.setIsActive(false);
        rawMaterialRepository.save(rawMaterial);
        log.info("Raw material deactivated: {}", rawMaterial.getCode());
    }

    private RawMaterialResponse mapToResponse(RawMaterial rawMaterial) {
        BigDecimal totalStock = stockRepository.getTotalStockQuantity(rawMaterial.getId());
        BigDecimal availableStock = stockRepository.getAvailableStockQuantity(rawMaterial.getId());

        RawMaterialResponse.CategoryInfo categoryInfo = null;
        if (rawMaterial.getCategory() != null) {
            categoryInfo = RawMaterialResponse.CategoryInfo.builder()
                    .id(rawMaterial.getCategory().getId())
                    .name(rawMaterial.getCategory().getName())
                    .code(rawMaterial.getCategory().getCode())
                    .build();
        }

        return RawMaterialResponse.builder()
                .id(rawMaterial.getId())
                .name(rawMaterial.getName())
                .code(rawMaterial.getCode())
                .description(rawMaterial.getDescription())
                .category(categoryInfo)
                .unit(RawMaterialResponse.UnitInfo.builder()
                        .id(rawMaterial.getUnit().getId())
                        .name(rawMaterial.getUnit().getName())
                        .symbol(rawMaterial.getUnit().getSymbol())
                        .build())
                .hsnCode(rawMaterial.getHsnCode())
                .reorderLevel(rawMaterial.getReorderLevel())
                .reorderQuantity(rawMaterial.getReorderQuantity())
                .minimumOrderQuantity(rawMaterial.getMinimumOrderQuantity())
                .leadTimeDays(rawMaterial.getLeadTimeDays())
                .standardCost(rawMaterial.getStandardCost())
                .lastPurchasePrice(rawMaterial.getLastPurchasePrice())
                .avgPurchasePrice(rawMaterial.getAvgPurchasePrice())
                .taxPercent(rawMaterial.getTaxPercent())
                .shelfLifeDays(rawMaterial.getShelfLifeDays())
                .storageConditions(rawMaterial.getStorageConditions())
                .isBatchTracked(rawMaterial.getIsBatchTracked())
                .isActive(rawMaterial.getIsActive())
                .totalStock(totalStock != null ? totalStock : BigDecimal.ZERO)
                .availableStock(availableStock != null ? availableStock : BigDecimal.ZERO)
                .createdAt(rawMaterial.getCreatedAt())
                .updatedAt(rawMaterial.getUpdatedAt())
                .build();
    }
}

