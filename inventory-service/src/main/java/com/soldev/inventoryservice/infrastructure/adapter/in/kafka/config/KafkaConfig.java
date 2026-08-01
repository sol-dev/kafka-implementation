package com.soldev.inventoryservice.infrastructure.adapter.in.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate){
        // 1. Recoverer: Envía el mensaje fallido a un topic con el sufijo "-dlt"
        // Ej: order.created.events -> order.created.events-dlt
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        // 2. Política de Reintentos: 2 reintentos con 1 segundo de espera entre ellos (Total: 3 intentos)
        FixedBackOff backOff = new FixedBackOff(1000L, 2L);

        // 3. Crear el handler
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        // Opcional: NO reintentar ante ciertas excepciones
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        return errorHandler;
    }

}
