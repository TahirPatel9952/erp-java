package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.GRNItemRequest;
import com.erp.manufacturing.dto.request.GRNRequest;
import com.erp.manufacturing.dto.response.GRNItemResponse;
import com.erp.manufacturing.dto.response.GRNResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.entity.*;
import com.erp.manufacturing.exception.BusinessException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.*;
import com.erp.manufacturing.service.GRNService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GRNServiceImpl implements GRNService {

    private final GoodsReceiptNoteRepository grnRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final UnitOfMeasurementRepository unitRepository;
    private final WarehouseLocationRepository locationRepository;
    private final RawMaterialStockRepository rawMaterialStockRepository;
    private final UserRepository userRepository;

    @Override
    public GRNResponse create(GRNRequest request) {
        log.info("Creating GRN for supplier: {}", request.getSupplierId());

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        PurchaseOrder purchaseOrder = null;
        if (request.getPurchaseOrderId() != null) {
            purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", request.getPurchaseOrderId()));
        }

        String grnNumber = generateGrnNumber();

        GoodsReceiptNote grn = GoodsReceiptNote.builder()
                .grnNumber(grnNumber)
                .purchaseOrder(purchaseOrder)
                .supplier(supplier)
                .warehouse(warehouse)
                .receiptDate(request.getReceiptDate())
                .status("DRAFT")
                .vehicleNo(request.getVehicleNo())
                .driverName(request.getDriverName())
                .challanNo(request.getChallanNo())
                .challanDate(request.getChallanDate())
                .notes(request.getNotes())
                .build();

        if (request.getItems() != null) {
            for (GRNItemRequest itemReq : request.getItems()) {
                GrnItem item = createGrnItem(itemReq, grn);
                grn.addItem(item);
            }
        }

        GoodsReceiptNote saved = grnRepository.save(grn);
        log.info("GRN created: {}", grnNumber);

        return mapToResponse(saved);
    }

    @Override
    public GRNResponse update(Long id, GRNRequest request) {
        GoodsReceiptNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRN", "id", id));

        if (!"DRAFT".equals(grn.getStatus())) {
            throw new BusinessException("Cannot update GRN in " + grn.getStatus() + " status");
        }

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        PurchaseOrder purchaseOrder = null;
        if (request.getPurchaseOrderId() != null) {
            purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", request.getPurchaseOrderId()));
        }

        grn.setPurchaseOrder(purchaseOrder);
        grn.setSupplier(supplier);
        grn.setWarehouse(warehouse);
        grn.setReceiptDate(request.getReceiptDate());
        grn.setVehicleNo(request.getVehicleNo());
        grn.setDriverName(request.getDriverName());
        grn.setChallanNo(request.getChallanNo());
        grn.setChallanDate(request.getChallanDate());
        grn.setNotes(request.getNotes());

        // Remove existing items
        grn.getItems().clear();

        // Add new items
        if (request.getItems() != null) {
            for (GRNItemRequest itemReq : request.getItems()) {
                GrnItem item = createGrnItem(itemReq, grn);
                grn.addItem(item);
            }
        }

        return mapToResponse(grnRepository.save(grn));
    }

    @Override
    @Transactional(readOnly = true)
    public GRNResponse getById(Long id) {
        return mapToResponse(grnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRN", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public GRNResponse getByGrnNumber(String grnNumber) {
        return mapToResponse(grnRepository.findByGrnNumber(grnNumber)
                .orElseThrow(() -> new ResourceNotFoundException("GRN", "grnNumber", grnNumber)));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<GRNResponse> getAll(Pageable pageable) {
        Page<GoodsReceiptNote> page = grnRepository.findAll(pageable);
        List<GRNResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<GRNResponse> search(String searchTerm, Pageable pageable) {
        Page<GoodsReceiptNote> page = grnRepository.search(searchTerm, pageable);
        List<GRNResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GRNResponse> getByPurchaseOrderId(Long purchaseOrderId) {
        return grnRepository.findByPurchaseOrderId(purchaseOrderId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GRNResponse> getByStatus(String status) {
        return grnRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GRNResponse> getPendingQC() {
        return grnRepository.findPendingQC().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GRNResponse verify(Long id) {
        // Fetch GRN with items eagerly to avoid lazy loading issues
        GoodsReceiptNote grn = grnRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRN", "id", id));

        String currentStatus = grn.getStatus();
        log.info("Verifying GRN {} with current status: {}", grn.getGrnNumber(), currentStatus);

        if (!"DRAFT".equals(currentStatus) && !"PENDING_QC".equals(currentStatus) && !"QC_COMPLETED".equals(currentStatus) && !"QC_IN_PROGRESS".equals(currentStatus)) {
            throw new BusinessException("Can only verify GRN in DRAFT, PENDING_QC, QC_IN_PROGRESS, or QC_COMPLETED status. Current status: " + currentStatus);
        }

        // Force load items to avoid lazy loading issues
        List<GrnItem> items = grn.getItems();
        if (items == null || items.isEmpty()) {
            throw new BusinessException("Cannot verify GRN without items");
        }

        log.info("GRN {} has {} items. Processing stock addition...", grn.getGrnNumber(), items.size());

        // Add stock to raw materials for accepted quantities BEFORE changing status
        int itemsProcessed = 0;
        int itemsSkipped = 0;
        for (GrnItem item : items) {
            // Force load lazy relationships
            RawMaterial rawMaterial = item.getRawMaterial();
            UnitOfMeasurement unit = item.getUnit();
            BigDecimal acceptedQty = item.getAcceptedQuantity();
            
            log.info("Processing GRN item {}: acceptedQuantity={}, receivedQuantity={}, rawMaterialId={}, rawMaterialName={}", 
                    item.getId(), acceptedQty, item.getReceivedQuantity(), rawMaterial.getId(), rawMaterial.getName());
            
            if (acceptedQty == null || acceptedQty.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Skipping GRN item {} - accepted quantity is null or zero: {}", item.getId(), acceptedQty);
                itemsSkipped++;
                continue;
            }
            
            try {
                addToRawMaterialStock(item, grn);
                itemsProcessed++;
                log.info("Successfully processed stock addition for GRN item {}", item.getId());
            } catch (Exception e) {
                log.error("Failed to add stock for item {}: {}", item.getId(), e.getMessage(), e);
                throw new BusinessException("Failed to add stock for item " + item.getId() + ": " + e.getMessage());
            }
        }

        if (itemsProcessed == 0) {
            log.warn("No items were processed for stock addition in GRN {}", grn.getGrnNumber());
            throw new BusinessException("Cannot verify GRN - no items with accepted quantity > 0");
        }

        // Set status to VERIFIED only after stock is successfully added
        grn.setStatus("VERIFIED");
        
        Long userId = getCurrentUserId();
        if (userId != null) {
            User verifier = userRepository.findById(userId).orElse(null);
            if (verifier != null) {
                grn.setQcBy(verifier);
                grn.setQcAt(LocalDateTime.now());
            }
        }

        GoodsReceiptNote saved = grnRepository.save(grn);
        log.info("GRN verified: {} - Stock added for {} items, {} items skipped", 
                grn.getGrnNumber(), itemsProcessed, itemsSkipped);

        return mapToResponse(saved);
    }

    @Override
    public GRNResponse cancel(Long id, String reason) {
        GoodsReceiptNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRN", "id", id));

        if ("VERIFIED".equals(grn.getStatus())) {
            throw new BusinessException("Cannot cancel verified GRN");
        }

        grn.setStatus("CANCELLED");
        grn.setNotes((grn.getNotes() != null ? grn.getNotes() + "\n" : "") + "Cancelled: " + reason);

        return mapToResponse(grnRepository.save(grn));
    }

    @Override
    public void delete(Long id) {
        GoodsReceiptNote grn = grnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GRN", "id", id));

        if (!"DRAFT".equals(grn.getStatus())) {
            throw new BusinessException("Can only delete GRN in DRAFT status");
        }

        grnRepository.delete(grn);
        log.info("GRN deleted: {}", id);
    }

    private GrnItem createGrnItem(GRNItemRequest itemReq, GoodsReceiptNote grn) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(itemReq.getRawMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "id", itemReq.getRawMaterialId()));

        UnitOfMeasurement unit = unitRepository.findById(itemReq.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit", "id", itemReq.getUnitId()));

        PurchaseOrderItem poItem = null;
        if (itemReq.getPurchaseOrderItemId() != null) {
            // Find PO item - would need PurchaseOrderItemRepository
        }

        WarehouseLocation location = null;
        if (itemReq.getLocationId() != null) {
            location = locationRepository.findById(itemReq.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse Location", "id", itemReq.getLocationId()));
        }

        return GrnItem.builder()
                .goodsReceiptNote(grn)
                .purchaseOrderItem(poItem)
                .rawMaterial(rawMaterial)
                .orderedQuantity(itemReq.getOrderedQuantity())
                .receivedQuantity(itemReq.getReceivedQuantity())
                .acceptedQuantity(itemReq.getAcceptedQuantity() != null ? itemReq.getAcceptedQuantity() : itemReq.getReceivedQuantity())
                .rejectedQuantity(itemReq.getRejectedQuantity() != null ? itemReq.getRejectedQuantity() : BigDecimal.ZERO)
                .unit(unit)
                .unitPrice(itemReq.getUnitPrice())
                .batchNo(itemReq.getBatchNo())
                .lotNo(itemReq.getLotNo())
                .manufacturingDate(itemReq.getManufacturingDate())
                .expiryDate(itemReq.getExpiryDate())
                .location(location)
                .qcStatus(itemReq.getQcStatus() != null ? itemReq.getQcStatus() : "PENDING")
                .rejectionReason(itemReq.getRejectionReason())
                .notes(itemReq.getNotes())
                .build();
    }

    private void addToRawMaterialStock(GrnItem item, GoodsReceiptNote grn) {
        try {
            RawMaterial rawMaterial = item.getRawMaterial();
            Warehouse warehouse = grn.getWarehouse();
            BigDecimal acceptedQty = item.getAcceptedQuantity();
            
            // Normalize batchNo: null or empty string becomes null
            String batchNo = (item.getBatchNo() != null && !item.getBatchNo().trim().isEmpty()) 
                    ? item.getBatchNo().trim() 
                    : null;
            Long locationId = item.getLocation() != null ? item.getLocation().getId() : null;

            log.info("Attempting to add stock - RawMaterialId: {}, WarehouseId: {}, LocationId: {}, BatchNo: {}, AcceptedQty: {}", 
                    rawMaterial.getId(), warehouse.getId(), locationId, batchNo, acceptedQty);

            if (acceptedQty == null || acceptedQty.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Skipping stock addition for GRN item {} - accepted quantity is zero or null: {}", item.getId(), acceptedQty);
                return;
            }

            // Find existing stock matching all unique constraint fields
            Optional<RawMaterialStock> existingStock = rawMaterialStockRepository
                    .findByRawMaterialIdAndWarehouseIdAndLocationIdAndBatchNo(
                            rawMaterial.getId(),
                            warehouse.getId(),
                            locationId,
                            batchNo);

            log.info("Stock lookup result - Found existing stock: {}", existingStock.isPresent());

            RawMaterialStock stock;
            if (existingStock.isPresent()) {
                stock = existingStock.get();
                BigDecimal oldQuantity = stock.getQuantity();
                stock.setQuantity(stock.getQuantity().add(acceptedQty));
                if (item.getUnitPrice() != null && item.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
                    stock.setUnitCost(item.getUnitPrice());
                }
                log.info("Updated existing stock ID {}: {} {} -> {} {} of {} in warehouse {} (location: {}, batch: {})", 
                        stock.getId(), oldQuantity, item.getUnit().getSymbol(), 
                        stock.getQuantity(), item.getUnit().getSymbol(), rawMaterial.getName(), 
                        warehouse.getName(), locationId != null ? locationId : "None", batchNo != null ? batchNo : "None");
            } else {
                stock = RawMaterialStock.builder()
                        .rawMaterial(rawMaterial)
                        .warehouse(warehouse)
                        .location(item.getLocation())
                        .quantity(acceptedQty)
                        .reservedQuantity(BigDecimal.ZERO)
                        .batchNo(batchNo)
                        .lotNo(item.getLotNo())
                        .manufacturingDate(item.getManufacturingDate())
                        .expiryDate(item.getExpiryDate())
                        .unitCost(item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO)
                        .build();
                log.info("Creating new stock: {} {} of {} in warehouse {} (location: {}, batch: {})", 
                        acceptedQty, item.getUnit().getSymbol(), rawMaterial.getName(), 
                        warehouse.getName(), locationId != null ? locationId : "None", batchNo != null ? batchNo : "None");
            }
            System.out.println("Stock: " + stock.toString());
            RawMaterialStock saved = rawMaterialStockRepository.saveAndFlush(stock);
            log.info("Successfully saved stock record ID: {} for raw material {} (ID: {}) in warehouse {} (ID: {}) - Final Quantity: {}", 
                    saved.getId(), rawMaterial.getName(), rawMaterial.getId(), warehouse.getName(), warehouse.getId(), saved.getQuantity());
            
            // Verify the save by reading it back
            Optional<RawMaterialStock> verifyStock = rawMaterialStockRepository.findById(saved.getId());
            if (verifyStock.isPresent()) {
                log.info("Verification: Stock record {} has quantity: {}", saved.getId(), verifyStock.get().getQuantity());
            } else {
                log.error("CRITICAL: Stock record {} was not found after save!", saved.getId());
            }
        } catch (Exception e) {
            log.error("Error adding stock for GRN item {}: {}", item.getId(), e.getMessage(), e);
            throw new BusinessException("Failed to add stock for raw material: " + e.getMessage());
        }
    }

    private String generateGrnNumber() {
        return "GRN-" + System.currentTimeMillis() % 100000000;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof com.erp.manufacturing.security.UserPrincipal) {
            return ((com.erp.manufacturing.security.UserPrincipal) auth.getPrincipal()).getId();
        }
        return null;
    }

    private GRNResponse mapToResponse(GoodsReceiptNote grn) {
        BigDecimal totalQty = grn.getItems().stream()
                .map(GrnItem::getReceivedQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal acceptedQty = grn.getItems().stream()
                .map(item -> item.getAcceptedQuantity() != null ? item.getAcceptedQuantity() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal rejectedQty = grn.getItems().stream()
                .map(item -> item.getRejectedQuantity() != null ? item.getRejectedQuantity() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = grn.getItems().stream()
                .map(item -> {
                    BigDecimal qty = item.getAcceptedQuantity() != null ? item.getAcceptedQuantity() : BigDecimal.ZERO;
                    BigDecimal price = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
                    return qty.multiply(price);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return GRNResponse.builder()
                .id(grn.getId())
                .grnNumber(grn.getGrnNumber())
                .purchaseOrderId(grn.getPurchaseOrder() != null ? grn.getPurchaseOrder().getId() : null)
                .purchaseOrderNumber(grn.getPurchaseOrder() != null ? grn.getPurchaseOrder().getPoNumber() : null)
                .supplierId(grn.getSupplier().getId())
                .supplierName(grn.getSupplier().getName())
                .supplierCode(grn.getSupplier().getCode())
                .warehouseId(grn.getWarehouse().getId())
                .warehouseName(grn.getWarehouse().getName())
                .receiptDate(grn.getReceiptDate())
                .status(grn.getStatus())
                .vehicleNo(grn.getVehicleNo())
                .driverName(grn.getDriverName())
                .challanNo(grn.getChallanNo())
                .challanDate(grn.getChallanDate())
                .notes(grn.getNotes())
                .qcNotes(grn.getQcNotes())
                .qcBy(grn.getQcBy() != null ? grn.getQcBy().getId() : null)
                .qcByName(grn.getQcBy() != null ? grn.getQcBy().getFullName() : null)
                .qcAt(grn.getQcAt())
                .totalItems(grn.getItems().size())
                .totalQuantity(totalQty)
                .acceptedQuantity(acceptedQty)
                .rejectedQuantity(rejectedQty)
                .totalAmount(totalAmount)
                .items(grn.getItems().stream().map(this::mapItemToResponse).collect(Collectors.toList()))
                .createdAt(grn.getCreatedAt())
                .updatedAt(grn.getUpdatedAt())
                .createdBy(grn.getCreatedBy())
                .updatedBy(grn.getUpdatedBy())
                .build();
    }

    private GRNItemResponse mapItemToResponse(GrnItem item) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (item.getAcceptedQuantity() != null && item.getUnitPrice() != null) {
            totalAmount = item.getAcceptedQuantity().multiply(item.getUnitPrice());
        }

        return GRNItemResponse.builder()
                .id(item.getId())
                .purchaseOrderItemId(item.getPurchaseOrderItem() != null ? item.getPurchaseOrderItem().getId() : null)
                .rawMaterialId(item.getRawMaterial().getId())
                .rawMaterialCode(item.getRawMaterial().getCode())
                .rawMaterialName(item.getRawMaterial().getName())
                .unitId(item.getUnit().getId())
                .unitName(item.getUnit().getName())
                .unitSymbol(item.getUnit().getSymbol())
                .orderedQuantity(item.getOrderedQuantity())
                .receivedQuantity(item.getReceivedQuantity())
                .acceptedQuantity(item.getAcceptedQuantity())
                .rejectedQuantity(item.getRejectedQuantity())
                .unitPrice(item.getUnitPrice())
                .totalAmount(totalAmount)
                .batchNo(item.getBatchNo())
                .lotNo(item.getLotNo())
                .manufacturingDate(item.getManufacturingDate())
                .expiryDate(item.getExpiryDate())
                .locationId(item.getLocation() != null ? item.getLocation().getId() : null)
                .locationCode(item.getLocation() != null ? item.getLocation().getLocationCode() : null)
                .qcStatus(item.getQcStatus())
                .rejectionReason(item.getRejectionReason())
                .notes(item.getNotes())
                .build();
    }
}
