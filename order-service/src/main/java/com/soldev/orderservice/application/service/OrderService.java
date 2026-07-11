package com.soldev.orderservice.application.service;

import com.soldev.orderservice.application.port.in.OrderCreatedUseCase;
import com.soldev.orderservice.application.port.out.OrderEventPublisherPort;
import com.soldev.orderservice.domain.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements OrderCreatedUseCase {

    private final OrderEventPublisherPort eventPublisherPort;

    public OrderService(OrderEventPublisherPort eventPublisherPort) {
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public void createOrder(Order order) {
        eventPublisherPort.publishOrderCreated(order);
    }
}
