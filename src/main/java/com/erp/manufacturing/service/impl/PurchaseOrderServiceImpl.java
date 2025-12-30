package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.PurchaseOrderItemRequest;
import com.erp.manufacturing.dto.request.PurchaseOrderRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.PurchaseOrderItemResponse;
import com.erp.manufacturing.dto.response.PurchaseOrderResponse;
import com.erp.manufacturing.entity.*;
import com.erp.manufacturing.enums.OrderStatus;
import com.erp.manufacturing.exception.BusinessException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.*;
import com.erp.manufacturing.security.UserPrincipal;
import com.erp.manufacturing.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final UserRepository userRepository;

    @Override
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {
        log.info("Creating purchase order for supplier: {}", request.getSupplierId());

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        String poNumber = generatePoNumber();

        PurchaseOrder po = PurchaseOrder.builder()
                .poNumber(poNumber)
                .supplier(supplier)
                .warehouse(warehouse)
                .orderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now())
                .expectedDate(request.getExpectedDate())
                .status(OrderStatus.DRAFT)
                .paymentTerms(request.getPaymentTerms())
                .deliveryTerms(request.getDeliveryTerms())
                .discountPercent(request.getDiscountPercent() != null ? request.getDiscountPercent() : BigDecimal.ZERO)
                .shippingCharges(request.getShippingCharges() != null ? request.getShippingCharges() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .internalNotes(request.getInternalNotes())
                .build();

        if (request.getItems() != null) {
            for (PurchaseOrderItemRequest itemReq : request.getItems()) {
                PurchaseOrderItem item = createPoItem(itemReq);
                po.addItem(item);
            }
        }

        po.calculateTotals();
        PurchaseOrder saved = purchaseOrderRepository.save(po);
        log.info("Purchase order created: {}", poNumber);

        return mapToResponse(saved);
    }

    @Override
    public PurchaseOrderResponse update(Long id, PurchaseOrderRequest request) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        if (po.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Cannot update purchase order in " + po.getStatus() + " status");
        }

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        po.setSupplier(supplier);
        po.setWarehouse(warehouse);
        po.setExpectedDate(request.getExpectedDate());
        po.setPaymentTerms(request.getPaymentTerms());
        po.setDeliveryTerms(request.getDeliveryTerms());
        po.setDiscountPercent(request.getDiscountPercent());
        po.setShippingCharges(request.getShippingCharges());
        po.setNotes(request.getNotes());
        po.setInternalNotes(request.getInternalNotes());

        if (request.getItems() != null) {
            po.getItems().clear();
            for (PurchaseOrderItemRequest itemReq : request.getItems()) {
                PurchaseOrderItem item = createPoItem(itemReq);
                po.addItem(item);
            }
        }

        po.calculateTotals();
        return mapToResponse(purchaseOrderRepository.save(po));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getById(Long id) {
        return mapToResponse(purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getByPoNumber(String poNumber) {
        return mapToResponse(purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "poNumber", poNumber)));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseOrderResponse> getAll(Pageable pageable) {
        Page<PurchaseOrder> page = purchaseOrderRepository.findAll(pageable);
        List<PurchaseOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseOrderResponse> search(String searchTerm, Pageable pageable) {
        Page<PurchaseOrder> page = purchaseOrderRepository.search(searchTerm, pageable);
        List<PurchaseOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseOrderResponse> getByStatus(String status, Pageable pageable) {
        Page<PurchaseOrder> page = purchaseOrderRepository.findByStatus(OrderStatus.valueOf(status), pageable);
        List<PurchaseOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseOrderResponse> getBySupplierId(Long supplierId, Pageable pageable) {
        Page<PurchaseOrder> page = purchaseOrderRepository.findBySupplierId(supplierId, pageable);
        List<PurchaseOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getPendingApproval() {
        return purchaseOrderRepository.findPendingApproval().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getPendingReceipt() {
        return purchaseOrderRepository.findPendingReceipt(LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseOrderResponse submit(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        if (po.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Can only submit purchase orders in DRAFT status");
        }

        po.setStatus(OrderStatus.PENDING_APPROVAL);
        return mapToResponse(purchaseOrderRepository.save(po));
    }

    @Override
    public PurchaseOrderResponse approve(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        if (po.getStatus() != OrderStatus.PENDING_APPROVAL) {
            throw new BusinessException("Can only approve purchase orders in PENDING_APPROVAL status");
        }

        Long userId = getCurrentUserId();
        User approver = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        po.setStatus(OrderStatus.APPROVED);
        po.setApprovedBy(approver);
        po.setApprovedAt(LocalDateTime.now());

        return mapToResponse(purchaseOrderRepository.save(po));
    }

    @Override
    public PurchaseOrderResponse reject(Long id, String reason) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        if (po.getStatus() != OrderStatus.PENDING_APPROVAL) {
            throw new BusinessException("Can only reject purchase orders in PENDING_APPROVAL status");
        }

        po.setStatus(OrderStatus.DRAFT);
        po.setInternalNotes((po.getInternalNotes() != null ? po.getInternalNotes() + "\n" : "") + "Rejected: " + reason);

        return mapToResponse(purchaseOrderRepository.save(po));
    }

    @Override
    public PurchaseOrderResponse sendToSupplier(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        if (po.getStatus() != OrderStatus.APPROVED) {
            throw new BusinessException("Can only send approved purchase orders to supplier");
        }

        po.setStatus(OrderStatus.ORDERED);
        return mapToResponse(purchaseOrderRepository.save(po));
    }

    @Override
    public PurchaseOrderResponse cancel(Long id, String reason) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        if (po.getStatus() == OrderStatus.RECEIVED || po.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel purchase order in " + po.getStatus() + " status");
        }

        po.setStatus(OrderStatus.CANCELLED);
        po.setInternalNotes((po.getInternalNotes() != null ? po.getInternalNotes() + "\n" : "") + "Cancelled: " + reason);

        return mapToResponse(purchaseOrderRepository.save(po));
    }

    @Override
    public void delete(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "id", id));

        if (po.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Can only delete purchase orders in DRAFT status");
        }

        purchaseOrderRepository.delete(po);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return purchaseOrderRepository.countByStatus(OrderStatus.valueOf(status));
    }

    private String generatePoNumber() {
        return "PO-" + System.currentTimeMillis() % 100000000;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) auth.getPrincipal()).getId();
        }
        return null;
    }

    private PurchaseOrderItem createPoItem(PurchaseOrderItemRequest request) {
        RawMaterial rm = rawMaterialRepository.findById(request.getRawMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", "id", request.getRawMaterialId()));

        return PurchaseOrderItem.builder()
                .rawMaterial(rm)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .taxPercent(request.getTaxPercent() != null ? request.getTaxPercent() : rm.getTaxPercent())
                .notes(request.getNotes())
                .build();
    }

    private PurchaseOrderResponse mapToResponse(PurchaseOrder po) {
        List<PurchaseOrderItemResponse> items = po.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        return PurchaseOrderResponse.builder()
                .id(po.getId())
                .poNumber(po.getPoNumber())
                .supplierId(po.getSupplier().getId())
                .supplierName(po.getSupplier().getName())
                .supplierCode(po.getSupplier().getCode())
                .warehouseId(po.getWarehouse().getId())
                .warehouseName(po.getWarehouse().getName())
                .orderDate(po.getOrderDate())
                .expectedDate(po.getExpectedDate())
                .status(po.getStatus().name())
                .paymentTerms(po.getPaymentTerms())
                .deliveryTerms(po.getDeliveryTerms())
                .subtotal(po.getSubtotal())
                .discountPercent(po.getDiscountPercent())
                .discountAmount(po.getDiscountAmount())
                .taxAmount(po.getTaxAmount())
                .shippingCharges(po.getShippingCharges())
                .grandTotal(po.getGrandTotal())
                .notes(po.getNotes())
                .internalNotes(po.getInternalNotes())
                .approvedBy(po.getApprovedBy() != null ? po.getApprovedBy().getId() : null)
                .approvedByName(po.getApprovedBy() != null ? po.getApprovedBy().getFirstName() + " " + po.getApprovedBy().getLastName() : null)
                .approvedAt(po.getApprovedAt())
                .items(items)
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .createdBy(po.getCreatedBy())
                .updatedBy(po.getUpdatedBy())
                .build();
    }

    private PurchaseOrderItemResponse mapItemToResponse(PurchaseOrderItem item) {
        RawMaterial rm = item.getRawMaterial();
        return PurchaseOrderItemResponse.builder()
                .id(item.getId())
                .rawMaterialId(rm.getId())
                .rawMaterialCode(rm.getCode())
                .rawMaterialName(rm.getName())
                .unitName(rm.getUnit() != null ? rm.getUnit().getName() : null)
                .unitSymbol(rm.getUnit() != null ? rm.getUnit().getSymbol() : null)
                .quantity(item.getQuantity())
                .receivedQuantity(item.getReceivedQuantity())
                .unitPrice(item.getUnitPrice())
                .taxPercent(item.getTaxPercent())
                .taxAmount(item.getTaxAmount())
                .total(item.getTotal())
                .notes(item.getNotes())
                .build();
    }
}

