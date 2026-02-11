package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.request.WorkOrderRequest;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.dto.response.WorkOrderResponse;
import com.erp.manufacturing.entity.*;
import com.erp.manufacturing.enums.WorkOrderStatus;
import com.erp.manufacturing.exception.BusinessException;
import com.erp.manufacturing.exception.ResourceNotFoundException;
import com.erp.manufacturing.repository.*;
import com.erp.manufacturing.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final BomHeaderRepository bomHeaderRepository;
    private final FinishedGoodsRepository finishedGoodsRepository;
    private final WarehouseRepository warehouseRepository;
    private final InProcessMaterialRepository inProcessMaterialRepository;
    private final InProcessStockRepository inProcessStockRepository;
    private final FinishedGoodsStockRepository finishedGoodsStockRepository;

    @Override
    public WorkOrderResponse create(WorkOrderRequest request) {
        log.info("Creating work order for finished goods: {}", request.getFinishedGoodsId());

        BomHeader bom = bomHeaderRepository.findById(request.getBomId())
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", request.getBomId()));

        FinishedGoods finishedGoods = finishedGoodsRepository.findById(request.getFinishedGoodsId())
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", request.getFinishedGoodsId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        String workOrderNo = generateWorkOrderNo();

        WorkOrder workOrder = WorkOrder.builder()
                .workOrderNo(workOrderNo)
                .bom(bom)
                .finishedGoods(finishedGoods)
                .warehouse(warehouse)
                .plannedQuantity(request.getPlannedQuantity())
                .completedQuantity(BigDecimal.ZERO)
                .rejectedQuantity(BigDecimal.ZERO)
                .status(WorkOrderStatus.DRAFT)
                .priority(request.getPriority() != null ? request.getPriority() : "NORMAL")
                .scheduledStartDate(request.getScheduledStartDate())
                .scheduledEndDate(request.getScheduledEndDate())
                .batchNo(request.getBatchNo())
                .notes(request.getNotes())
                .build();

        WorkOrder saved = workOrderRepository.save(workOrder);
        log.info("Work order created: {}", workOrderNo);

        return mapToResponse(saved);
    }

    @Override
    public WorkOrderResponse update(Long id, WorkOrderRequest request) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id));

        if (workOrder.getStatus() != WorkOrderStatus.DRAFT && workOrder.getStatus() != WorkOrderStatus.PLANNED) {
            throw new BusinessException("Cannot update work order in " + workOrder.getStatus() + " status");
        }

        BomHeader bom = bomHeaderRepository.findById(request.getBomId())
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", request.getBomId()));

        FinishedGoods finishedGoods = finishedGoodsRepository.findById(request.getFinishedGoodsId())
                .orElseThrow(() -> new ResourceNotFoundException("Finished Goods", "id", request.getFinishedGoodsId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

        workOrder.setBom(bom);
        workOrder.setFinishedGoods(finishedGoods);
        workOrder.setWarehouse(warehouse);
        workOrder.setPlannedQuantity(request.getPlannedQuantity());
        workOrder.setPriority(request.getPriority());
        workOrder.setScheduledStartDate(request.getScheduledStartDate());
        workOrder.setScheduledEndDate(request.getScheduledEndDate());
        workOrder.setBatchNo(request.getBatchNo());
        workOrder.setNotes(request.getNotes());

        return mapToResponse(workOrderRepository.save(workOrder));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderResponse getById(Long id) {
        return mapToResponse(workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderResponse getByWorkOrderNo(String workOrderNo) {
        return mapToResponse(workOrderRepository.findByWorkOrderNo(workOrderNo)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "workOrderNo", workOrderNo)));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WorkOrderResponse> getAll(Pageable pageable) {
        Page<WorkOrder> page = workOrderRepository.findAll(pageable);
        List<WorkOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WorkOrderResponse> search(String searchTerm, Pageable pageable) {
        Page<WorkOrder> page = workOrderRepository.search(searchTerm, pageable);
        List<WorkOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WorkOrderResponse> getByStatus(String status, Pageable pageable) {
        Page<WorkOrder> page = workOrderRepository.findByStatus(WorkOrderStatus.valueOf(status), pageable);
        List<WorkOrderResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> getInProgress() {
        return workOrderRepository.findInProgressWorkOrders().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> getPending() {
        return workOrderRepository.findPendingWorkOrders(LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkOrderResponse release(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id));

        if (workOrder.getStatus() != WorkOrderStatus.DRAFT && workOrder.getStatus() != WorkOrderStatus.PLANNED) {
            throw new BusinessException("Can only release work orders in DRAFT or PLANNED status");
        }

        workOrder.setStatus(WorkOrderStatus.RELEASED);
        return mapToResponse(workOrderRepository.save(workOrder));
    }

    @Override
    public WorkOrderResponse start(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id));

        if (workOrder.getStatus() != WorkOrderStatus.RELEASED) {
            throw new BusinessException("Can only start work orders in RELEASED status");
        }

        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        workOrder.setActualStartDate(LocalDateTime.now());
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        // Create In-Process Stock entry when work order starts
        createInProcessStock(savedWorkOrder);

        log.info("Work order started: {} - In-process stock created", savedWorkOrder.getWorkOrderNo());
        return mapToResponse(savedWorkOrder);
    }

    private void createInProcessStock(WorkOrder workOrder) {
        FinishedGoods finishedGoods = workOrder.getFinishedGoods();
        
        // Find or create InProcessMaterial for this finished goods
        InProcessMaterial inProcessMaterial = inProcessMaterialRepository.findByCode(finishedGoods.getCode())
                .orElseGet(() -> {
                    // Create new InProcessMaterial if it doesn't exist
                    InProcessMaterial newMaterial = InProcessMaterial.builder()
                            .name(finishedGoods.getName() + " (In-Process)")
                            .code(finishedGoods.getCode() + "-WIP")
                            .description("In-process material for " + finishedGoods.getName())
                            .stage("PRODUCTION")
                            .unit(finishedGoods.getUnit())
                            .standardCost(finishedGoods.getStandardCost())
                            .isActive(true)
                            .build();
                    return inProcessMaterialRepository.save(newMaterial);
                });

        // Check if InProcessStock already exists for this work order
        Optional<InProcessStock> existingStock = inProcessStockRepository
                .findByWorkOrderIdAndInProcessMaterialId(workOrder.getId(), inProcessMaterial.getId());

        if (existingStock.isEmpty()) {
            // Create new InProcessStock entry
            InProcessStock inProcessStock = InProcessStock.builder()
                    .inProcessMaterial(inProcessMaterial)
                    .warehouse(workOrder.getWarehouse())
                    .workOrder(workOrder)
                    .quantity(workOrder.getPlannedQuantity())
                    .stage("PRODUCTION")
                    .batchNo(workOrder.getBatchNo())
                    .build();
            inProcessStockRepository.save(inProcessStock);
            log.info("Created in-process stock for work order: {} - Quantity: {}", 
                    workOrder.getWorkOrderNo(), workOrder.getPlannedQuantity());
        } else {
            // Update existing stock quantity
            InProcessStock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity().add(workOrder.getPlannedQuantity()));
            inProcessStockRepository.save(stock);
            log.info("Updated in-process stock for work order: {} - New Quantity: {}", 
                    workOrder.getWorkOrderNo(), stock.getQuantity());
        }
    }

    @Override
    public WorkOrderResponse complete(Long id, BigDecimal completedQuantity, BigDecimal rejectedQuantity) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id));

        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new BusinessException("Can only complete work orders in IN_PROGRESS status");
        }

        workOrder.setCompletedQuantity(completedQuantity);
        workOrder.setRejectedQuantity(rejectedQuantity != null ? rejectedQuantity : BigDecimal.ZERO);
        workOrder.setStatus(WorkOrderStatus.COMPLETED);
        workOrder.setActualEndDate(LocalDateTime.now());
        
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        
        // Create Finished Goods Stock entry when work order completes
        if (completedQuantity.compareTo(BigDecimal.ZERO) > 0) {
            createFinishedGoodsStock(savedWorkOrder, completedQuantity);
        }
        
        // Remove from In-Process Stock
        removeFromInProcessStock(savedWorkOrder);

        log.info("Work order completed: {} - Finished goods stock created: {}", 
                savedWorkOrder.getWorkOrderNo(), completedQuantity);
        
        return mapToResponse(savedWorkOrder);
    }
    
    private void createFinishedGoodsStock(WorkOrder workOrder, BigDecimal quantity) {
        FinishedGoods finishedGoods = workOrder.getFinishedGoods();
        Warehouse warehouse = workOrder.getWarehouse();
        
        // Find existing stock or create new
        Optional<FinishedGoodsStock> existingStock = finishedGoodsStockRepository
                .findByFinishedGoodsAndWarehouseAndBatch(
                        finishedGoods.getId(),
                        warehouse.getId(),
                        workOrder.getBatchNo());
        
        if (existingStock.isPresent()) {
            // Update existing stock
            FinishedGoodsStock stock = existingStock.get();
            stock.setQuantity(stock.getQuantity().add(quantity));
            stock.setManufacturingDate(workOrder.getActualEndDate() != null ? 
                    workOrder.getActualEndDate().toLocalDate() : LocalDate.now());
            finishedGoodsStockRepository.save(stock);
            log.info("Updated finished goods stock for work order: {} - Added quantity: {}", 
                    workOrder.getWorkOrderNo(), quantity);
        } else {
            // Create new stock entry
            FinishedGoodsStock stock = FinishedGoodsStock.builder()
                    .finishedGoods(finishedGoods)
                    .warehouse(warehouse)
                    .quantity(quantity)
                    .reservedQuantity(BigDecimal.ZERO)
                    .batchNo(workOrder.getBatchNo())
                    .manufacturingDate(workOrder.getActualEndDate() != null ? 
                            workOrder.getActualEndDate().toLocalDate() : LocalDate.now())
                    .unitCost(finishedGoods.getStandardCost() != null ? 
                            finishedGoods.getStandardCost() : BigDecimal.ZERO)
                    .build();
            finishedGoodsStockRepository.save(stock);
            log.info("Created finished goods stock for work order: {} - Quantity: {}", 
                    workOrder.getWorkOrderNo(), quantity);
        }
    }
    
    private void removeFromInProcessStock(WorkOrder workOrder) {
        List<InProcessStock> inProcessStocks = inProcessStockRepository.findByWorkOrderId(workOrder.getId());
        for (InProcessStock stock : inProcessStocks) {
            // Reduce quantity or remove if quantity becomes zero
            if (stock.getQuantity().compareTo(workOrder.getCompletedQuantity()) <= 0) {
                inProcessStockRepository.delete(stock);
            } else {
                stock.setQuantity(stock.getQuantity().subtract(workOrder.getCompletedQuantity()));
                inProcessStockRepository.save(stock);
            }
        }
        log.info("Removed in-process stock for completed work order: {}", workOrder.getWorkOrderNo());
    }

    @Override
    public WorkOrderResponse cancel(Long id, String reason) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id));

        if (workOrder.getStatus() == WorkOrderStatus.COMPLETED) {
            throw new BusinessException("Cannot cancel completed work orders");
        }

        workOrder.setStatus(WorkOrderStatus.CANCELLED);
        workOrder.setNotes((workOrder.getNotes() != null ? workOrder.getNotes() + "\n" : "") + "Cancelled: " + reason);

        return mapToResponse(workOrderRepository.save(workOrder));
    }

    @Override
    public void delete(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work Order", "id", id));

        if (workOrder.getStatus() != WorkOrderStatus.DRAFT) {
            throw new BusinessException("Can only delete work orders in DRAFT status");
        }

        workOrderRepository.delete(workOrder);
        log.info("Work order deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return workOrderRepository.countByStatus(WorkOrderStatus.valueOf(status));
    }

    private String generateWorkOrderNo() {
        return "WO-" + System.currentTimeMillis() % 100000000;
    }

    private WorkOrderResponse mapToResponse(WorkOrder wo) {
        return WorkOrderResponse.builder()
                .id(wo.getId())
                .workOrderNo(wo.getWorkOrderNo())
                .bomId(wo.getBom().getId())
                .bomCode(wo.getBom().getBomCode())
                .finishedGoodsId(wo.getFinishedGoods().getId())
                .finishedGoodsCode(wo.getFinishedGoods().getCode())
                .finishedGoodsName(wo.getFinishedGoods().getName())
                .warehouseId(wo.getWarehouse().getId())
                .warehouseName(wo.getWarehouse().getName())
                .plannedQuantity(wo.getPlannedQuantity())
                .completedQuantity(wo.getCompletedQuantity())
                .rejectedQuantity(wo.getRejectedQuantity())
                .pendingQuantity(wo.getPendingQuantity())
                .completionPercentage(wo.getCompletionPercentage())
                .status(wo.getStatus().name())
                .priority(wo.getPriority())
                .scheduledStartDate(wo.getScheduledStartDate())
                .scheduledEndDate(wo.getScheduledEndDate())
                .actualStartDate(wo.getActualStartDate())
                .actualEndDate(wo.getActualEndDate())
                .batchNo(wo.getBatchNo())
                .notes(wo.getNotes())
                .createdAt(wo.getCreatedAt())
                .updatedAt(wo.getUpdatedAt())
                .createdBy(wo.getCreatedBy())
                .updatedBy(wo.getUpdatedBy())
                .build();
    }
}

