package com.soldev.orderservice.infrastructure.adapter.in.rest;

import com.soldev.orderservice.application.port.in.OrderCreatedUseCase;
import com.soldev.orderservice.domain.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderCreatedUseCase createOrderUseCase;

    public OrderController(OrderCreatedUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping
    public void createOrder(Order order){
        createOrderUseCase.createOrder(order);
    }
}
