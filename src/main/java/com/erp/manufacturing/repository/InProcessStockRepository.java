package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.InProcessStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InProcessStockRepository extends JpaRepository<InProcessStock, Long> {

    List<InProcessStock> findByWorkOrderId(Long workOrderId);

    List<InProcessStock> findByWarehouseId(Long warehouseId);

    Optional<InProcessStock> findByWorkOrderIdAndInProcessMaterialId(Long workOrderId, Long inProcessMaterialId);

    @Query("SELECT s FROM InProcessStock s JOIN s.workOrder wo WHERE wo.status = 'IN_PROGRESS'")
    List<InProcessStock> findActiveInProcessStock();
    
    @Query("SELECT s FROM InProcessStock s JOIN s.workOrder wo WHERE wo.status = 'IN_PROGRESS'")
    Page<InProcessStock> findActiveInProcessStock(Pageable pageable);
    
    @Query("SELECT s FROM InProcessStock s JOIN s.workOrder wo WHERE wo.status = 'IN_PROGRESS' AND s.warehouse.id = :warehouseId")
    Page<InProcessStock> findActiveInProcessStockByWarehouse(@Param("warehouseId") Long warehouseId, Pageable pageable);

    @Query("SELECT SUM(s.quantity) FROM InProcessStock s WHERE s.workOrder.id = :workOrderId")
    BigDecimal getTotalQuantityByWorkOrder(@Param("workOrderId") Long workOrderId);
}
