package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.InProcessMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InProcessMaterialRepository extends JpaRepository<InProcessMaterial, Long> {
    Optional<InProcessMaterial> findByCode(String code);
    boolean existsByCode(String code);
}
