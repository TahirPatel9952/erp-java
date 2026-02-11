package com.erp.manufacturing.repository;

import com.erp.manufacturing.entity.DeliveryChallan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryChallanRepository extends JpaRepository<DeliveryChallan, Long> {
    Optional<DeliveryChallan> findByChallanNumber(String challanNumber);
}
