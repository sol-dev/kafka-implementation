package com.soldev.inventoryservice.infrastructure.adapter.out.persistence.repository;

import com.soldev.inventoryservice.infrastructure.adapter.out.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, String> {

}
