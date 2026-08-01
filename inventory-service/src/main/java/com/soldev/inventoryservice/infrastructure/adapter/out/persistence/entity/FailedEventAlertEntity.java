package com.soldev.inventoryservice.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_event_alert")
@NoArgsConstructor
@Getter
@Setter
public class FailedEventAlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;
    private String orderId;
    private String originalTopic;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    private LocalDateTime failedAt;

    public FailedEventAlertEntity(String eventId, String orderId, String originalTopic, String errorMessage) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.originalTopic = originalTopic;
        this.errorMessage = errorMessage;
        this.failedAt = LocalDateTime.now();
    }
}
