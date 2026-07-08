package com.soldev.kafka_implementation.infrastructure.adapter.in.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class UserEventConsumer implements Consumer<Message<GenericRecord>> {

    @Override
    @KafkaListener(topics = "${app.kafka.topics.user}",
            groupId = "${app.kafka.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void accept(Message<GenericRecord> genericRecordMessage) {
        log.info("Kafka Event Message : {}", genericRecordMessage.toString());
    }
}