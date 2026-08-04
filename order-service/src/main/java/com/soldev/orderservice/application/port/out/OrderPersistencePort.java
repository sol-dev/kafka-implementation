package com.soldev.orderservice.application.port.out;

import com.soldev.orderservice.domain.model.Order;

public interface OrderPersistencePort {

    Long createOrder(Order order);
}
