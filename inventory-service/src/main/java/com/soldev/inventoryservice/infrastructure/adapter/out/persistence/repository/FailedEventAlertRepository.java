package com.soldev.inventoryservice.infrastructure.adapter.out.persistence.repository;

import com.soldev.inventoryservice.infrastructure.adapter.out.persistence.entity.FailedEventAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedEventAlertRepository extends JpaRepository<FailedEventAlertEntity, Long> {
}