package com.erp.manufacturing.service.impl;

import com.erp.manufacturing.dto.response.InProcessInventoryResponse;
import com.erp.manufacturing.dto.response.PageResponse;
import com.erp.manufacturing.entity.InProcessStock;
import com.erp.manufacturing.entity.WorkOrder;
import com.erp.manufacturing.enums.WorkOrderStatus;
import com.erp.manufacturing.repository.InProcessStockRepository;
import com.erp.manufacturing.repository.WorkOrderRepository;
import com.erp.manufacturing.service.InProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InProcessServiceImpl implements InProcessService {

    private final InProcessStockRepository inProcessStockRepository;
    private final WorkOrderRepository workOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InProcessInventoryResponse> getAllActive() {
        List<InProcessStock> stocks = inProcessStockRepository.findActiveInProcessStock();
        return stocks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InProcessInventoryResponse> getAll(Pageable pageable) {
        Page<InProcessStock> page = inProcessStockRepository.findActiveInProcessStock(pageable);
        List<InProcessInventoryResponse> responses = page.getContent().stream()
                .map(this::mapToResponse)
                .filter(response -> response != null) // Filter out null responses
                .collect(Collectors.toList());
        return PageResponse.of(responses, page);
    }

    @Override
    @Transactional(readOnly = true)
    public InProcessInventoryResponse getByWorkOrderId(Long workOrderId) {
        List<InProcessStock> stocks = inProcessStockRepository.findByWorkOrderId(workOrderId);
        if (stocks.isEmpty()) {
            // Return response based on work order if no stock record exists
            WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                    .orElseThrow(() -> new RuntimeException("Work order not found"));
            
            if (workOrder.getStatus() == WorkOrderStatus.IN_PROGRESS) {
                return InProcessInventoryResponse.builder()
                        .workOrderId(workOrder.getId())
                        .workOrderNumber(workOrder.getWorkOrderNo())
                        .finishedGoodsId(workOrder.getFinishedGoods().getId())
                        .finishedGoodsCode(workOrder.getFinishedGoods().getCode())
                        .finishedGoodsName(workOrder.getFinishedGoods().getName())
                        .warehouseId(workOrder.getWarehouse().getId())
                        .warehouseName(workOrder.getWarehouse().getName())
                        .quantity(workOrder.getPlannedQuantity())
                        .unitId(workOrder.getFinishedGoods().getUnit().getId())
                        .unitName(workOrder.getFinishedGoods().getUnit().getName())
                        .unitSymbol(workOrder.getFinishedGoods().getUnit().getSymbol())
                        .startDate(workOrder.getActualStartDate())
                        .expectedEndDate(workOrder.getScheduledEndDate() != null ? 
                                workOrder.getScheduledEndDate().atStartOfDay() : null)
                        .status(workOrder.getStatus().name())
                        .currentStage("IN_PROGRESS")
                        .build();
            }
            return null;
        }
        return mapToResponse(stocks.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InProcessInventoryResponse> getByWarehouseId(Long warehouseId) {
        List<InProcessStock> stocks = inProcessStockRepository.findByWarehouseId(warehouseId);
        return stocks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InProcessInventoryResponse mapToResponse(InProcessStock stock) {
        WorkOrder workOrder = stock.getWorkOrder();
        if (workOrder == null) {
            return null;
        }

        return InProcessInventoryResponse.builder()
                .id(stock.getId())
                .workOrderId(workOrder.getId())
                .workOrderNumber(workOrder.getWorkOrderNo())
                .finishedGoodsId(workOrder.getFinishedGoods().getId())
                .finishedGoodsCode(workOrder.getFinishedGoods().getCode())
                .finishedGoodsName(workOrder.getFinishedGoods().getName())
                .warehouseId(stock.getWarehouse().getId())
                .warehouseName(stock.getWarehouse().getName())
                .currentStage(stock.getStage())
                .quantity(stock.getQuantity())
                .unitId(workOrder.getFinishedGoods().getUnit().getId())
                .unitName(workOrder.getFinishedGoods().getUnit().getName())
                .unitSymbol(workOrder.getFinishedGoods().getUnit().getSymbol())
                .startDate(workOrder.getActualStartDate())
                .expectedEndDate(workOrder.getScheduledEndDate() != null ? 
                        workOrder.getScheduledEndDate().atStartOfDay() : null)
                .status(workOrder.getStatus().name())
                .batchNo(stock.getBatchNo())
                .build();
    }
}
