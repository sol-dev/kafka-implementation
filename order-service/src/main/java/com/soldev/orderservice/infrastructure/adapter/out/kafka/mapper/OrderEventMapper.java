package com.soldev.orderservice.infrastructure.adapter.out.kafka.mapper;

import com.soldev.orderservice.domain.model.Order;
import com.soldev.sharedevents.OrderCreatedEventAvro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderEventMapper {

    @Mapping(target = "eventId", source = ".", qualifiedByName = "generateEventId")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "mapLocalDateTimeToString")
    OrderCreatedEventAvro toAvro(Order order);

    @Named("generateEventId")
    default String generateEventId(Order order) {
        // Garantía de arquitectura: Generamos el UUID único del evento aquí
        return UUID.randomUUID().toString();
    }

    @Named("mapLocalDateTimeToString")
    default String mapLocalDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

}

