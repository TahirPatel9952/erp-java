package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.WorkOrder;
import com.erp.manufacturing.enums.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    Optional<WorkOrder> findByWorkOrderNo(String workOrderNo);

    boolean existsByWorkOrderNo(String workOrderNo);

    Page<WorkOrder> findByStatus(WorkOrderStatus status, Pageable pageable);

    Page<WorkOrder> findByFinishedGoodsId(Long finishedGoodsId, Pageable pageable);

    @Query("SELECT wo FROM WorkOrder wo WHERE " +
           "LOWER(wo.workOrderNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(wo.finishedGoods.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(wo.finishedGoods.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<WorkOrder> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status IN :statuses")
    List<WorkOrder> findByStatusIn(@Param("statuses") List<WorkOrderStatus> statuses);

    @Query("SELECT wo FROM WorkOrder wo WHERE wo.scheduledStartDate BETWEEN :startDate AND :endDate")
    List<WorkOrder> findByScheduledDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(wo) FROM WorkOrder wo WHERE wo.status = :status")
    long countByStatus(@Param("status") WorkOrderStatus status);

    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status = 'IN_PROGRESS' ORDER BY wo.actualStartDate ASC")
    List<WorkOrder> findInProgressWorkOrders();

    @Query("SELECT wo FROM WorkOrder wo WHERE wo.status IN ('PLANNED', 'RELEASED') AND wo.scheduledStartDate <= :date ORDER BY wo.scheduledStartDate ASC")
    List<WorkOrder> findPendingWorkOrders(@Param("date") LocalDate date);
}

