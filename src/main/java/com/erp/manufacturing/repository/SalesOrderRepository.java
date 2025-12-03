package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.SalesOrder;
import com.erp.manufacturing.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    Optional<SalesOrder> findBySoNumber(String soNumber);

    boolean existsBySoNumber(String soNumber);

    Page<SalesOrder> findByStatus(OrderStatus status, Pageable pageable);

    Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT so FROM SalesOrder so WHERE " +
           "LOWER(so.soNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(so.customer.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<SalesOrder> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT so FROM SalesOrder so WHERE so.status IN :statuses")
    List<SalesOrder> findByStatusIn(@Param("statuses") List<OrderStatus> statuses);

    @Query("SELECT so FROM SalesOrder so WHERE so.orderDate BETWEEN :startDate AND :endDate")
    List<SalesOrder> findByOrderDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(so) FROM SalesOrder so WHERE so.status = :status")
    long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT so FROM SalesOrder so WHERE so.status IN ('CONFIRMED', 'PROCESSING') ORDER BY so.deliveryDate ASC NULLS LAST")
    List<SalesOrder> findPendingDelivery();

    @Query("SELECT SUM(so.grandTotal) FROM SalesOrder so WHERE so.orderDate BETWEEN :startDate AND :endDate AND so.status NOT IN ('DRAFT', 'CANCELLED')")
    BigDecimal getTotalSalesValue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT so.customer.id, SUM(so.grandTotal) FROM SalesOrder so WHERE so.orderDate BETWEEN :startDate AND :endDate AND so.status NOT IN ('DRAFT', 'CANCELLED') GROUP BY so.customer.id ORDER BY SUM(so.grandTotal) DESC")
    List<Object[]> getTopCustomersBySales(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
}

