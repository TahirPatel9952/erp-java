package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.FinishedGoodsStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinishedGoodsStockRepository extends JpaRepository<FinishedGoodsStock, Long> {
    
    List<FinishedGoodsStock> findByFinishedGoodsId(Long finishedGoodsId);
    
    List<FinishedGoodsStock> findByWarehouseId(Long warehouseId);
    
    Optional<FinishedGoodsStock> findByFinishedGoodsIdAndWarehouseIdAndBatchNo(
            Long finishedGoodsId, Long warehouseId, String batchNo);
    
    @Query("SELECT fgs FROM FinishedGoodsStock fgs WHERE fgs.quantity > 0")
    List<FinishedGoodsStock> findAllWithStock();
    
    @Query("SELECT fgs FROM FinishedGoodsStock fgs WHERE fgs.finishedGoods.id = :finishedGoodsId AND fgs.quantity > 0")
    List<FinishedGoodsStock> findWithStockByFinishedGoodsId(@Param("finishedGoodsId") Long finishedGoodsId);
    
    @Query("SELECT fgs FROM FinishedGoodsStock fgs WHERE fgs.warehouse.id = :warehouseId AND fgs.quantity > 0")
    List<FinishedGoodsStock> findWithStockByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    @Query("SELECT SUM(fgs.quantity) FROM FinishedGoodsStock fgs WHERE fgs.finishedGoods.id = :finishedGoodsId")
    BigDecimal getTotalQuantityByFinishedGoodsId(@Param("finishedGoodsId") Long finishedGoodsId);
    
    @Query("SELECT SUM(fgs.quantity) FROM FinishedGoodsStock fgs WHERE fgs.warehouse.id = :warehouseId")
    BigDecimal getTotalQuantityByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    @Query("SELECT fgs FROM FinishedGoodsStock fgs WHERE fgs.finishedGoods.id = :finishedGoodsId " +
           "AND fgs.warehouse.id = :warehouseId AND (fgs.batchNo = :batchNo OR (:batchNo IS NULL AND fgs.batchNo IS NULL))")
    Optional<FinishedGoodsStock> findByFinishedGoodsAndWarehouseAndBatch(
            @Param("finishedGoodsId") Long finishedGoodsId,
            @Param("warehouseId") Long warehouseId,
            @Param("batchNo") String batchNo);
}
