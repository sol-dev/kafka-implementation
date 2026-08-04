package com.soldev.orderservice.infrastructure.adapter.out.mysql.adapter;

import com.soldev.orderservice.application.port.out.OrderPersistencePort;
import com.soldev.orderservice.domain.model.Order;
import com.soldev.orderservice.infrastructure.adapter.out.mysql.entity.OrderEntity;
import com.soldev.orderservice.infrastructure.adapter.out.mysql.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class OrderPersistenceAdapter implements OrderPersistencePort {

    Logger logger = LoggerFactory.getLogger(OrderPersistenceAdapter.class);
    private final OrderRepository repository;

    public OrderPersistenceAdapter(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long createOrder(Order order) {
        logger.info("Saving order on database.");
        OrderEntity savedEntity = repository.save(map(order));
        return nonNull(savedEntity) ? savedEntity.getId() : null;
    }

    private OrderEntity map(Order model) {
        OrderEntity entity = new OrderEntity();
        entity.setCustomerId(model.getCustomerId());
        entity.setTotalAmount(model.getTotalAmount());
        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }

}
