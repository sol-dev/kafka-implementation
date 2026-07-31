package com.soldev.orderservice.application.service;

import com.soldev.orderservice.application.port.in.OrderCreatedUseCase;
import com.soldev.orderservice.application.port.out.OrderEventPublisherPort;
import com.soldev.orderservice.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements OrderCreatedUseCase {

    private final OrderEventPublisherPort eventPublisherPort;
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderEventPublisherPort eventPublisherPort) {
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    public void createOrder(Order order) {
        logger.info("ORDER-SERVICE: Create Order");
        eventPublisherPort.publishOrderCreated(order);
    }
}
