package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.BomHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BomHeaderRepository extends JpaRepository<BomHeader, Long> {

    Optional<BomHeader> findByBomCode(String bomCode);

    boolean existsByBomCode(String bomCode);

    List<BomHeader> findByFinishedGoodsId(Long finishedGoodsId);

    @Query("SELECT b FROM BomHeader b WHERE b.finishedGoods.id = :finishedGoodsId AND b.isActive = true")
    Optional<BomHeader> findActiveByFinishedGoodsId(@Param("finishedGoodsId") Long finishedGoodsId);

    @Query("SELECT b FROM BomHeader b WHERE " +
           "LOWER(b.bomCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.finishedGoods.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.finishedGoods.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<BomHeader> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT b FROM BomHeader b WHERE b.isActive = true")
    List<BomHeader> findAllActive();

    Page<BomHeader> findByIsActive(Boolean isActive, Pageable pageable);
}

