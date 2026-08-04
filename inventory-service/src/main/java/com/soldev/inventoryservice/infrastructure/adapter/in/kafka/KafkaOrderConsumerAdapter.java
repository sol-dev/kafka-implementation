package com.soldev.inventoryservice.infrastructure.adapter.in.kafka;

import com.soldev.inventoryservice.domain.model.OrderResolution;
import com.soldev.inventoryservice.infrastructure.adapter.out.persistence.adapter.KafkaOrderProcessorTrx;
import com.soldev.sharedevents.OrderCreatedEventAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class KafkaOrderConsumerAdapter {

    //Responsabilidad: Listener, deserializar Avro y confirmar el offset (ack).
    private static final Logger log = LoggerFactory.getLogger(KafkaOrderConsumerAdapter.class);

    private final KafkaOrderProcessorTrx idempotentProcessor;

    public KafkaOrderConsumerAdapter(KafkaOrderProcessorTrx idempotentProcessor) {
        this.idempotentProcessor = idempotentProcessor;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(ConsumerRecord<String, OrderCreatedEventAvro> record, Acknowledgment ack) {
        OrderCreatedEventAvro avroEvent = record.value();
        String partitionKey = record.key();

        log.info("Mensaje Recibido de Kafka en particion {} con Key (orderId): {}", record.partition(), partitionKey);

        try {
            //Mapear de avro a modelo de dominio
            OrderResolution orderResolution = new OrderResolution(
                    avroEvent.getOrderId(),
                    avroEvent.getCustomerId().toString(),
                    BigDecimal.valueOf(avroEvent.getTotalAmount())
            );

            // Llamamos al componente de infraestructura que maneja la transacción e idempotencia
            idempotentProcessor.processIdempotent(orderResolution, avroEvent.getEventId().toString());


            // AT-LEAST-ONCE semantics; confirmamos a kafka SOLO SI el dominio procesó con exito,
            log.info("Haciendo commit manual del offset para el evento: {}", avroEvent.getEventId());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error procesando el evento {}. Kafka reintentará la entrega", avroEvent.getEventId(), e);
            // Al NO llamar a ack.acknowledge(), cuando el contenedor reinicie o venza el timeout,
            // Kafka reentregará el mensaje (Cumpliendo el flujo At-Least-Once).

            // TODO: configurar una Dead Letter Queue (DLQ)
            // lanzar la excepción para que el ErrorHandler de Spring envíe el mensaje al DLT de forma automática.
            throw e;
        }
    }
}