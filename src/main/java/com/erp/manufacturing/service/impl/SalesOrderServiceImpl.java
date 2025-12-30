package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.SalesOrderItemRequest;
import com.erp.manufacturing.dto.request.SalesOrderRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.SalesOrderItemResponse;
import com.erp.manufacturing.dto.response.SalesOrderResponse;
import com.erp.manufacturing.entity.*;
import com.erp.manufacturing.enums.OrderStatus;
import com.erp.manufacturing.exception.BusinessException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.*;
import com.erp.manufacturing.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final WarehouseRepository warehouseRepository;
    private final FinishedGoodsRepository finishedGoodsRepository;

    @Override
    public SalesOrderResponse create(SalesOrderRequest request) {
        log.info("Creating sales order for customer: {}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));

        Warehouse warehouse = null;
        if (request.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));
        }

        String soNumber = generateSoNumber();

        SalesOrder so = SalesOrder.builder()
                .soNumber(soNumber)
                .customer(customer)
                .warehouse(warehouse)
                .orderDate(request.getOrderDate() != null ? request.getOrderDate() : LocalDate.now())
                .deliveryDate(request.getDeliveryDate())
                .status(OrderStatus.DRAFT)
                .shippingAddress(request.getShippingAddress() != null ? request.getShippingAddress() : customer.getShippingAddress())
                .paymentTerms(request.getPaymentTerms())
                .discountPercent(request.getDiscountPercent() != null ? request.getDiscountPercent() : BigDecimal.ZERO)
                .shippingCharges(request.getShippingCharges() != null ? request.getShippingCharges() : BigDecimal.ZERO)
                .notes(request.getNotes())
                .internalNotes(request.getInternalNotes())
                .build();

        if (request.getItems() != null) {
            for (SalesOrderItemRequest itemReq : request.getItems()) {
                SalesOrderItem item = createSoItem(itemReq);
                so.addItem(item);
            }
        }

        so.calculateTotals();
        SalesOrder saved = salesOrderRepository.save(so);
        log.info("Sales order created: {}", soNumber);

        return mapToResponse(saved);
    }

    @Override
    public SalesOrderResponse update(Long id, SalesOrderRequest request) {
        SalesOrder so = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "id", id));

        if (so.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Cannot update sales order in " + so.getStatus() + " status");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));

        Warehouse warehouse = null;
        if (request.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));
        }

        so.setCustomer(customer);
        so.setWarehouse(warehouse);
        so.setDeliveryDate(request.getDeliveryDate());
        so.setShippingAddress(request.getShippingAddress());
        so.setPaymentTerms(request.getPaymentTerms());
        so.setDiscountPercent(request.getDiscountPercent());
        so.setShippingCharges(request.getShippingCharges());
        so.setNotes(request.getNotes());
        so.setInternalNotes(request.getInternalNotes());

        if (request.getItems() != null) {
            so.getItems().clear();
            for (SalesOrderItemRequest itemReq : request.getItems()) {
                SalesOrderItem item = createSoItem(itemReq);
                so.addItem(item);
            }
        }

        so.calculateTotals();
        return mapToResponse(salesOrderRepository.save(so));
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderResponse getById(Long id) {
        return mapToResponse(salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderResponse getBySoNumber(String soNumber) {
        return mapToResponse(salesOrderRepository.findBySoNumber(soNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "soNumber", soNumber)));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SalesOrderResponse> getAll(Pageable pageable) {
        Page<SalesOrder> page = salesOrderRepository.findAll(pageable);
        List<SalesOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SalesOrderResponse> search(String searchTerm, Pageable pageable) {
        Page<SalesOrder> page = salesOrderRepository.search(searchTerm, pageable);
        List<SalesOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SalesOrderResponse> getByStatus(String status, Pageable pageable) {
        Page<SalesOrder> page = salesOrderRepository.findByStatus(OrderStatus.valueOf(status), pageable);
        List<SalesOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SalesOrderResponse> getByCustomerId(Long customerId, Pageable pageable) {
        Page<SalesOrder> page = salesOrderRepository.findByCustomerId(customerId, pageable);
        List<SalesOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponse> getPendingDelivery() {
        return salesOrderRepository.findPendingDelivery().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponse> getByStatusList(List<String> statuses) {
        List<OrderStatus> orderStatuses = statuses.stream()
                .map(OrderStatus::valueOf)
                .collect(Collectors.toList());
        return salesOrderRepository.findByStatusIn(orderStatuses).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SalesOrderResponse confirm(Long id) {
        SalesOrder so = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "id", id));

        if (so.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Can only confirm sales orders in DRAFT status");
        }

        so.setStatus(OrderStatus.CONFIRMED);
        return mapToResponse(salesOrderRepository.save(so));
    }

    @Override
    public SalesOrderResponse process(Long id) {
        SalesOrder so = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "id", id));

        if (so.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException("Can only process sales orders in CONFIRMED status");
        }

        so.setStatus(OrderStatus.PROCESSING);
        return mapToResponse(salesOrderRepository.save(so));
    }

    @Override
    public SalesOrderResponse cancel(Long id, String reason) {
        SalesOrder so = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "id", id));

        if (so.getStatus() == OrderStatus.DELIVERED || so.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel sales order in " + so.getStatus() + " status");
        }

        so.setStatus(OrderStatus.CANCELLED);
        so.setInternalNotes((so.getInternalNotes() != null ? so.getInternalNotes() + "\n" : "") + "Cancelled: " + reason);

        return mapToResponse(salesOrderRepository.save(so));
    }

    @Override
    public void delete(Long id) {
        SalesOrder so = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales Order", "id", id));

        if (so.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("Can only delete sales orders in DRAFT status");
        }

        salesOrderRepository.delete(so);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return salesOrderRepository.countByStatus(OrderStatus.valueOf(status));
    }

    private String generateSoNumber() {
        return "SO-" + System.currentTimeMillis() % 100000000;
    }

    private SalesOrderItem createSoItem(SalesOrderItemRequest request) {
        FinishedGoods fg = finishedGoodsRepository.findById(request.getFinishedGoodsId())
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", request.getFinishedGoodsId()));

        return SalesOrderItem.builder()
                .finishedGoods(fg)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .discountPercent(request.getDiscountPercent() != null ? request.getDiscountPercent() : BigDecimal.ZERO)
                .taxPercent(request.getTaxPercent() != null ? request.getTaxPercent() : fg.getTaxPercent())
                .notes(request.getNotes())
                .build();
    }

    private SalesOrderResponse mapToResponse(SalesOrder so) {
        List<SalesOrderItemResponse> items = so.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        return SalesOrderResponse.builder()
                .id(so.getId())
                .soNumber(so.getSoNumber())
                .customerId(so.getCustomer().getId())
                .customerName(so.getCustomer().getName())
                .customerCode(so.getCustomer().getCode())
                .warehouseId(so.getWarehouse() != null ? so.getWarehouse().getId() : null)
                .warehouseName(so.getWarehouse() != null ? so.getWarehouse().getName() : null)
                .orderDate(so.getOrderDate())
                .deliveryDate(so.getDeliveryDate())
                .status(so.getStatus().name())
                .shippingAddress(so.getShippingAddress())
                .paymentTerms(so.getPaymentTerms())
                .subtotal(so.getSubtotal())
                .discountPercent(so.getDiscountPercent())
                .discountAmount(so.getDiscountAmount())
                .taxAmount(so.getTaxAmount())
                .shippingCharges(so.getShippingCharges())
                .grandTotal(so.getGrandTotal())
                .notes(so.getNotes())
                .internalNotes(so.getInternalNotes())
                .items(items)
                .createdAt(so.getCreatedAt())
                .updatedAt(so.getUpdatedAt())
                .createdBy(so.getCreatedBy())
                .updatedBy(so.getUpdatedBy())
                .build();
    }

    private SalesOrderItemResponse mapItemToResponse(SalesOrderItem item) {
        FinishedGoods fg = item.getFinishedGoods();
        return SalesOrderItemResponse.builder()
                .id(item.getId())
                .finishedGoodsId(fg.getId())
                .finishedGoodsCode(fg.getCode())
                .finishedGoodsName(fg.getName())
                .unitName(fg.getUnit() != null ? fg.getUnit().getName() : null)
                .unitSymbol(fg.getUnit() != null ? fg.getUnit().getSymbol() : null)
                .quantity(item.getQuantity())
                .deliveredQuantity(item.getDeliveredQuantity())
                .unitPrice(item.getUnitPrice())
                .discountPercent(item.getDiscountPercent())
                .discountAmount(item.getQuantity().multiply(item.getUnitPrice()).multiply(item.getDiscountPercent()).divide(new BigDecimal("100")))
                .taxPercent(item.getTaxPercent())
                .taxAmount(item.getTaxAmount())
                .total(item.getTotal())
                .notes(item.getNotes())
                .build();
    }
}

