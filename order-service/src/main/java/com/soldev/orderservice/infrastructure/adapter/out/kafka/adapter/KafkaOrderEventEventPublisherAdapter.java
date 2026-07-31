package com.soldev.orderservice.infrastructure.adapter.out.kafka.adapter;

import com.soldev.orderservice.application.port.out.OrderEventPublisherPort;
import com.soldev.orderservice.domain.model.Order;
import com.soldev.orderservice.infrastructure.adapter.out.kafka.mapper.OrderEventMapper;
import com.soldev.sharedevents.OrderCreatedEventAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderEventEventPublisherAdapter implements OrderEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaOrderEventEventPublisherAdapter.class);

    private final KafkaTemplate<String, OrderCreatedEventAvro> kafkaTemplate;
    private final OrderEventMapper orderEventMapper;
    private final String topic;

    public KafkaOrderEventEventPublisherAdapter(KafkaTemplate<String, OrderCreatedEventAvro> kafkaTemplate, OrderEventMapper orderEventMapper, @Value("${app.kafka.topic}")String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventMapper = orderEventMapper;
        this.topic = topic;
    }

    @Override
    public void publishOrderCreated(Order order) {
        log.info("ORDER-PUBLISHER: publishing event for order {}", order.toString());
        // 1. Transformamos al contrato Avro
        OrderCreatedEventAvro avroEvent = orderEventMapper.toAvro(order);

        // 2. Garantía de orden: Usamos el ID de la orden como Partition Key
        String key = order.getId();

        log.info("Publicando evento de Orden Creada en Kafka. Topic: {}, Partition Key: {}, EventId: {}",
                topic, key, avroEvent.getEventId());

        // 3. Enviamos de forma asíncrona pero aprovechando que acks=all e idempotence=true están en el yml
        this.kafkaTemplate.send(topic, key, avroEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error crítico enviando evento a Kafka para orden id: {}", key, ex);
                    } else {
                        log.info("Evento enviado exitosamente. Partition: {}, Offset: {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

}
