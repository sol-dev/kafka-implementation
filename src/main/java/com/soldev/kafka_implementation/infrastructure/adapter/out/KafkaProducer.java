package com.soldev.kafka_implementation.infrastructure.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, GenericRecord event) {
        String uuid = UUID.randomUUID().toString();
        CompletableFuture<SendResult<String, GenericRecord>> sendResultFuture = kafkaTemplate.send(topic, uuid, event);

        sendResultFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Record sent to topic: {}. Record: {}. Record UUID: {}", topic, event, uuid);
            }else {
                log.error("Error sending record to topic: {}. Record: {}. Record UUID: {}", topic, event, uuid);
            }
        });
    }

}
