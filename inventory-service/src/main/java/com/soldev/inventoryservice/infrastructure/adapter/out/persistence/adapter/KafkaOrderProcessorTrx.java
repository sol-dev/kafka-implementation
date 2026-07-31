package com.soldev.inventoryservice.infrastructure.adapter.out.persistence.adapter;


import com.soldev.inventoryservice.application.port.in.ProcessOrderUseCase;
import com.soldev.inventoryservice.domain.model.OrderResolution;
import com.soldev.inventoryservice.infrastructure.adapter.out.persistence.entity.ProcessedEventEntity;
import com.soldev.inventoryservice.infrastructure.adapter.out.persistence.repository.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KafkaOrderProcessorTrx {

    // Responsabilidad: Abrir transacción de DB, validar duplicados técnicos y salvar el eventId.
    private static final Logger log = LoggerFactory.getLogger(KafkaOrderProcessorTrx.class);

    private final ProcessedEventRepository processedEventRepository;
    private final ProcessOrderUseCase processOrderUseCase;

    public KafkaOrderProcessorTrx(ProcessedEventRepository processedEventRepository, ProcessOrderUseCase processOrderUseCase) {
        this.processedEventRepository = processedEventRepository;
        this.processOrderUseCase = processOrderUseCase;
    }

    @Transactional
    public void processIdempotent(OrderResolution orderResolution, String eventId){
        log.info("KafkaOrderProcessorTrx - Idempotencia: Procesando evento {}", eventId);

        // 1. Control técnico de idempotencia
        if (processedEventRepository.existsById(eventId)) {
            log.warn("RESULTADO: Evento {} ya procesado. Ignorando de forma segura.", eventId);
            return;
        }

        log.info("RESULTADO: Mensaje sin procesar.", eventId);
        // 2. Invocar al dominio puro
        processOrderUseCase.process(orderResolution);

        // 3. Registrar el evento técnico consumido
        processedEventRepository.save(new ProcessedEventEntity(eventId));
    }


}
