package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.PurchaseOrder;
import com.erp.manufacturing.enums.OrderStatus;
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
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByPoNumber(String poNumber);

    boolean existsByPoNumber(String poNumber);

    Page<PurchaseOrder> findByStatus(OrderStatus status, Pageable pageable);

    Page<PurchaseOrder> findBySupplierId(Long supplierId, Pageable pageable);

    @Query("SELECT po FROM PurchaseOrder po WHERE " +
           "LOWER(po.poNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(po.supplier.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PurchaseOrder> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.status IN :statuses")
    List<PurchaseOrder> findByStatusIn(@Param("statuses") List<OrderStatus> statuses);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.orderDate BETWEEN :startDate AND :endDate")
    List<PurchaseOrder> findByOrderDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(po) FROM PurchaseOrder po WHERE po.status = :status")
    long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = 'PENDING_APPROVAL' ORDER BY po.createdAt ASC")
    List<PurchaseOrder> findPendingApproval();

    @Query("SELECT po FROM PurchaseOrder po WHERE po.status IN ('APPROVED', 'ORDERED') AND po.expectedDate <= :date ORDER BY po.expectedDate ASC")
    List<PurchaseOrder> findPendingReceipt(@Param("date") LocalDate date);

    @Query("SELECT SUM(po.grandTotal) FROM PurchaseOrder po WHERE po.orderDate BETWEEN :startDate AND :endDate AND po.status NOT IN ('DRAFT', 'CANCELLED')")
    java.math.BigDecimal getTotalPurchaseValue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

