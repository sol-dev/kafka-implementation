package com.soldev.inventoryservice.infrastructure.adapter.in.kafka;


import com.soldev.inventoryservice.infrastructure.adapter.out.persistence.entity.FailedEventAlertEntity;
import com.soldev.inventoryservice.infrastructure.adapter.out.persistence.repository.FailedEventAlertRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KafkaDLQConsumerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(KafkaDLQConsumerAdapter.class);
    private final static String DLQ_GROUP = "inventory-dlq-group";
    private final FailedEventAlertRepository repository;

    public KafkaDLQConsumerAdapter(FailedEventAlertRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @KafkaListener(
            topics = "${app.kafka.topic}-dlt",
            groupId = DLQ_GROUP
    )
    public void onDlqMessage(
            ConsumerRecord<String, com.soldev.sharedevents.OrderCreatedEventAvro> record,
            @Header(value = KafkaHeaders.DLT_EXCEPTION_MESSAGE, required = false) String exceptionMessage,
            @Header(value = KafkaHeaders.DLT_ORIGINAL_TOPIC, required = false) String originalTopic,
            Acknowledgment ack
    ) {
        com.soldev.sharedevents.OrderCreatedEventAvro event = record.value();
        String eventId = event.getEventId().toString();
        String orderId = String.valueOf(event.getOrderId());

        logger.error("Procesando mensaje fallido en DLQ. EventId: {}, Error: {}", eventId, exceptionMessage);
        try {
            // 1. Guardar la alerta en la base de datos
            FailedEventAlertEntity alert = new FailedEventAlertEntity(
                    eventId,
                    orderId,
                    originalTopic != null ? originalTopic : "Desconocido",
                    exceptionMessage != null ? exceptionMessage : "Sin detalle de error"
            );

            repository.save(alert);
            logger.info("Alerta de evento fallido guardada en base de datos correctamente.");

            // 2. Confirmar a Kafka que la alerta fue registrada - commit manual
            // seguimos respetando la semántica At-Least-Once.
            // Si la base se cae al intentar guardar la alerta, el ack no se ejecuta
            // y el mensaje se queda seguro en la DLQ hasta que la base de datos vuelva.
            ack.acknowledge();

        } catch (Exception e) {
            logger.error("Error catastrófico al intentar guardar la alerta de la DLQ para el evento {}", eventId, e);
            // Si falla guardar la alerta, lanzamos la excepción para que Kafka lo reintente en este topic
            throw e;
        }

    }

}
