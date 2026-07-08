package com.soldev.kafka_implementation.infrastructure.adapter;

import lombok.Getter;

@Getter
public enum KafkaTopicStatic {
    USER_CREATED_TOPIC("user.events");

    private final String topic;

    KafkaTopicStatic(String topic){
        this.topic = topic;
    }

}
