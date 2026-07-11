package com.soldev.orderservice.application.port.in;

import com.soldev.orderservice.domain.model.Order;

public interface OrderCreatedUseCase {

    void createOrder(Order order);
}
