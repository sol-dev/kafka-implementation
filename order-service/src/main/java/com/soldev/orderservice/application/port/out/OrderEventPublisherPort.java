package com.soldev.orderservice.application.port.out;

import com.soldev.orderservice.domain.model.Order;

public interface OrderEventPublisherPort {

    void publishOrderCreated(Order order);
}
