package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.Category;
import com.erp.manufacturing.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCode(String code);

    boolean existsByCode(String code);

    List<Category> findByType(CategoryType type);

    List<Category> findByTypeAndIsActive(CategoryType type, Boolean isActive);

    List<Category> findByParentIdIsNull();

    List<Category> findByParentIsNull();

    List<Category> findByParentId(Long parentId);

    List<Category> findByIsActiveTrue();

    @Query("SELECT c FROM Category c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Category> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.isActive = true AND c.type = :type")
    List<Category> findActiveByType(@Param("type") CategoryType type);
}

