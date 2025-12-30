package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.BOMItemRequest;
import com.erp.manufacturing.dto.request.BOMRequest;
import com.erp.manufacturing.dto.response.BOMItemResponse;
import com.erp.manufacturing.dto.response.BOMResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.entity.BomDetail;
import com.erp.manufacturing.entity.BomHeader;
import com.erp.manufacturing.entity.FinishedGoods;
import com.erp.manufacturing.entity.RawMaterial;
import com.erp.manufacturing.entity.UnitOfMeasurement;
import com.erp.manufacturing.exception.DuplicateResourceException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.BomHeaderRepository;
import com.erp.manufacturing.repository.FinishedGoodsRepository;
import com.erp.manufacturing.repository.RawMaterialRepository;
import com.erp.manufacturing.repository.UnitOfMeasurementRepository;
import com.erp.manufacturing.service.BOMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BOMServiceImpl implements BOMService {

    private final BomHeaderRepository bomHeaderRepository;
    private final FinishedGoodsRepository finishedGoodsRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final UnitOfMeasurementRepository unitRepository;

    @Override
    public BOMResponse create(BOMRequest request) {
        log.info("Creating BOM with code: {}", request.getCode());

        if (bomHeaderRepository.existsByBomCode(request.getCode())) {
            throw new DuplicateResourceException("BOM", "code", request.getCode());
        }

        FinishedGoods finishedGoods = finishedGoodsRepository.findById(request.getFinishedGoodsId())
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", request.getFinishedGoodsId()));

        UnitOfMeasurement outputUnit = null;
        if (request.getOutputUnitId() != null) {
            outputUnit = unitRepository.findById(request.getOutputUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getOutputUnitId()));
        }

        BomHeader bomHeader = BomHeader.builder()
                .bomCode(request.getCode())
                .finishedGoods(finishedGoods)
                .version(request.getVersion() != null ? request.getVersion() : "1.0")
                .description(request.getDescription())
                .outputQuantity(request.getOutputQuantity() != null ? request.getOutputQuantity() : BigDecimal.ONE)
                .outputUnit(outputUnit)
                .effectiveFrom(request.getEffectiveFrom())
                .effectiveTo(request.getEffectiveTo())
                .standardTimeMinutes(request.getStandardTimeMinutes() != null ? request.getStandardTimeMinutes() : 0)
                .setupTimeMinutes(request.getSetupTimeMinutes() != null ? request.getSetupTimeMinutes() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        // Add items if provided
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (BOMItemRequest itemRequest : request.getItems()) {
                BomDetail detail = createBomDetail(itemRequest);
                bomHeader.addDetail(detail);
            }
        }

        BomHeader saved = bomHeaderRepository.save(bomHeader);
        log.info("BOM created successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public BOMResponse update(Long id, BOMRequest request) {
        log.info("Updating BOM with id: {}", id);

        BomHeader bomHeader = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", id));

        // Check for duplicate code
        if (!bomHeader.getBomCode().equals(request.getCode()) && bomHeaderRepository.existsByBomCode(request.getCode())) {
            throw new DuplicateResourceException("BOM", "code", request.getCode());
        }

        FinishedGoods finishedGoods = finishedGoodsRepository.findById(request.getFinishedGoodsId())
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", request.getFinishedGoodsId()));

        UnitOfMeasurement outputUnit = null;
        if (request.getOutputUnitId() != null) {
            outputUnit = unitRepository.findById(request.getOutputUnitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getOutputUnitId()));
        }

        bomHeader.setBomCode(request.getCode());
        bomHeader.setFinishedGoods(finishedGoods);
        bomHeader.setVersion(request.getVersion());
        bomHeader.setDescription(request.getDescription());
        bomHeader.setOutputQuantity(request.getOutputQuantity());
        bomHeader.setOutputUnit(outputUnit);
        bomHeader.setEffectiveFrom(request.getEffectiveFrom());
        bomHeader.setEffectiveTo(request.getEffectiveTo());
        bomHeader.setStandardTimeMinutes(request.getStandardTimeMinutes());
        bomHeader.setSetupTimeMinutes(request.getSetupTimeMinutes());

        if (request.getIsActive() != null) {
            bomHeader.setIsActive(request.getIsActive());
        }

        // Update items
        if (request.getItems() != null) {
            bomHeader.getDetails().clear();
            for (BOMItemRequest itemRequest : request.getItems()) {
                BomDetail detail = createBomDetail(itemRequest);
                bomHeader.addDetail(detail);
            }
        }

        BomHeader saved = bomHeaderRepository.save(bomHeader);
        log.info("BOM updated successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BOMResponse getById(Long id) {
        BomHeader bomHeader = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", id));
        return mapToResponse(bomHeader);
    }

    @Override
    @Transactional(readOnly = true)
    public BOMResponse getByCode(String code) {
        BomHeader bomHeader = bomHeaderRepository.findByBomCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "code", code));
        return mapToResponse(bomHeader);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BOMResponse> getAll(Pageable pageable) {
        Page<BomHeader> page = bomHeaderRepository.findAll(pageable);
        List<BOMResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BOMResponse> search(String searchTerm, Pageable pageable) {
        Page<BomHeader> page = bomHeaderRepository.search(searchTerm, pageable);
        List<BOMResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BOMResponse> getAllActive() {
        return bomHeaderRepository.findAllActive().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BOMResponse> getByFinishedGoodsId(Long finishedGoodsId) {
        return bomHeaderRepository.findByFinishedGoodsId(finishedGoodsId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BOMResponse getActiveByFinishedGoodsId(Long finishedGoodsId) {
        BomHeader bomHeader = bomHeaderRepository.findActiveByFinishedGoodsId(finishedGoodsId)
                .orElseThrow(() -> new ResourceNotFoundException("Active BOM", "finishedGoodsId", finishedGoodsId));
        return mapToResponse(bomHeader);
    }

    @Override
    public BOMResponse activate(Long id) {
        BomHeader bomHeader = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", id));
        bomHeader.setIsActive(true);
        return mapToResponse(bomHeaderRepository.save(bomHeader));
    }

    @Override
    public BOMResponse deactivate(Long id) {
        BomHeader bomHeader = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", id));
        bomHeader.setIsActive(false);
        return mapToResponse(bomHeaderRepository.save(bomHeader));
    }

    @Override
    public BOMResponse duplicate(Long id, String newVersion) {
        BomHeader original = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", id));

        String newCode = original.getBomCode() + "-" + newVersion;
        if (bomHeaderRepository.existsByBomCode(newCode)) {
            throw new DuplicateResourceException("BOM", "code", newCode);
        }

        BomHeader copy = BomHeader.builder()
                .bomCode(newCode)
                .finishedGoods(original.getFinishedGoods())
                .version(newVersion)
                .description(original.getDescription())
                .outputQuantity(original.getOutputQuantity())
                .outputUnit(original.getOutputUnit())
                .standardTimeMinutes(original.getStandardTimeMinutes())
                .setupTimeMinutes(original.getSetupTimeMinutes())
                .isActive(false) // New version starts as inactive
                .details(new ArrayList<>())
                .build();

        // Copy details
        for (BomDetail detail : original.getDetails()) {
            BomDetail copyDetail = BomDetail.builder()
                    .itemType(detail.getItemType())
                    .itemId(detail.getItemId())
                    .sequenceNo(detail.getSequenceNo())
                    .quantity(detail.getQuantity())
                    .unit(detail.getUnit())
                    .wastagePercent(detail.getWastagePercent())
                    .isCritical(detail.getIsCritical())
                    .notes(detail.getNotes())
                    .build();
            copy.addDetail(copyDetail);
        }

        BomHeader saved = bomHeaderRepository.save(copy);
        log.info("BOM duplicated successfully. New id: {}, new version: {}", saved.getId(), newVersion);

        return mapToResponse(saved);
    }

    @Override
    public void delete(Long id) {
        BomHeader bomHeader = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", id));
        bomHeaderRepository.delete(bomHeader);
        log.info("BOM deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return bomHeaderRepository.existsByBomCode(code);
    }

    private BomDetail createBomDetail(BOMItemRequest request) {
        UnitOfMeasurement unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", request.getUnitId()));

        return BomDetail.builder()
                .itemType(request.getItemType())
                .itemId(request.getItemId())
                .sequenceNo(request.getSequenceNo() != null ? request.getSequenceNo() : 0)
                .quantity(request.getQuantity())
                .unit(unit)
                .wastagePercent(request.getWastagePercent() != null ? request.getWastagePercent() : BigDecimal.ZERO)
                .isCritical(request.getIsCritical() != null ? request.getIsCritical() : false)
                .notes(request.getNotes())
                .build();
    }

    private BOMResponse mapToResponse(BomHeader bomHeader) {
        FinishedGoods fg = bomHeader.getFinishedGoods();
        UnitOfMeasurement outputUnit = bomHeader.getOutputUnit();

        List<BOMItemResponse> itemResponses = bomHeader.getDetails().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        BigDecimal totalMaterialCost = itemResponses.stream()
                .map(item -> item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BOMResponse.builder()
                .id(bomHeader.getId())
                .code(bomHeader.getBomCode())
                .version(bomHeader.getVersion())
                .description(bomHeader.getDescription())
                .finishedGoodsId(fg.getId())
                .finishedGoodsCode(fg.getCode())
                .finishedGoodsName(fg.getName())
                .outputQuantity(bomHeader.getOutputQuantity())
                .outputUnitId(outputUnit != null ? outputUnit.getId() : null)
                .outputUnitName(outputUnit != null ? outputUnit.getName() : null)
                .outputUnitSymbol(outputUnit != null ? outputUnit.getSymbol() : null)
                .effectiveFrom(bomHeader.getEffectiveFrom())
                .effectiveTo(bomHeader.getEffectiveTo())
                .standardTimeMinutes(bomHeader.getStandardTimeMinutes())
                .setupTimeMinutes(bomHeader.getSetupTimeMinutes())
                .isActive(bomHeader.getIsActive())
                .status(bomHeader.getIsActive() ? "ACTIVE" : "INACTIVE")
                .totalMaterialCost(totalMaterialCost)
                .items(itemResponses)
                .createdAt(bomHeader.getCreatedAt())
                .updatedAt(bomHeader.getUpdatedAt())
                .createdBy(bomHeader.getCreatedBy())
                .updatedBy(bomHeader.getUpdatedBy())
                .build();
    }

    private BOMItemResponse mapItemToResponse(BomDetail detail) {
        String itemCode = "";
        String itemName = "";
        BigDecimal unitPrice = BigDecimal.ZERO;

        // Get item details based on type
        if ("RAW_MATERIAL".equals(detail.getItemType())) {
            RawMaterial rm = rawMaterialRepository.findById(detail.getItemId()).orElse(null);
            if (rm != null) {
                itemCode = rm.getCode();
                itemName = rm.getName();
                unitPrice = rm.getStandardCost();
            }
        }
        // Add handling for other item types as needed

        UnitOfMeasurement unit = detail.getUnit();
        BigDecimal quantityWithWastage = detail.getQuantityWithWastage();
        BigDecimal totalPrice = quantityWithWastage.multiply(unitPrice);

        return BOMItemResponse.builder()
                .id(detail.getId())
                .itemType(detail.getItemType())
                .itemId(detail.getItemId())
                .itemCode(itemCode)
                .itemName(itemName)
                .sequenceNo(detail.getSequenceNo())
                .quantity(detail.getQuantity())
                .quantityWithWastage(quantityWithWastage)
                .unitId(unit != null ? unit.getId() : null)
                .unitName(unit != null ? unit.getName() : null)
                .unitSymbol(unit != null ? unit.getSymbol() : null)
                .wastagePercent(detail.getWastagePercent())
                .isCritical(detail.getIsCritical())
                .notes(detail.getNotes())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build();
    }
}

