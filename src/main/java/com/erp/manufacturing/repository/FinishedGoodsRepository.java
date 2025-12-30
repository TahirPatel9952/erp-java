package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.FinishedGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinishedGoodsRepository extends JpaRepository<FinishedGoods, Long> {

    Optional<FinishedGoods> findByCode(String code);

    Optional<FinishedGoods> findByBarcode(String barcode);

    boolean existsByCode(String code);

    boolean existsByBarcode(String barcode);

    @Query("SELECT fg FROM FinishedGoods fg WHERE " +
           "LOWER(fg.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(fg.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(fg.barcode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(fg.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<FinishedGoods> search(@Param("search") String search, Pageable pageable);

    Page<FinishedGoods> findByCategoryId(Long categoryId, Pageable pageable);

    Page<FinishedGoods> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT fg FROM FinishedGoods fg WHERE fg.isActive = true AND fg.category.id = :categoryId")
    List<FinishedGoods> findActiveByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT fg FROM FinishedGoods fg WHERE fg.isActive = true")
    List<FinishedGoods> findAllActive();

    @Query("SELECT fg FROM FinishedGoods fg JOIN FinishedGoodsStock fgs ON fg.id = fgs.finishedGoods.id " +
           "WHERE fgs.quantity <= fg.reorderLevel AND fg.isActive = true")
    List<FinishedGoods> findLowStockItems();

    @Query("SELECT COUNT(fg) FROM FinishedGoods fg WHERE fg.isActive = true")
    long countActive();

    List<FinishedGoods> findByIsActiveTrue();
}

