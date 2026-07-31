package com.soldev.inventoryservice.infrastructure.adapter.out.persistence.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_events")
@NoArgsConstructor
@Getter
@Setter
// CLASE PARA IDEMPOTENCIA
public class ProcessedEventEntity {

    @Id
    private String eventId; // El UUID generado por el productor
    private LocalDateTime processedAt;

    public ProcessedEventEntity(String eventId) {
        this.eventId = eventId;
        this.processedAt = LocalDateTime.now();
    }


}
