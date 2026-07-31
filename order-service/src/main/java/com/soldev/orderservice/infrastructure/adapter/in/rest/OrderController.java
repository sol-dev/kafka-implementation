package com.soldev.orderservice.infrastructure.adapter.in.rest;

import com.soldev.orderservice.application.port.in.OrderCreatedUseCase;
import com.soldev.orderservice.application.service.OrderService;
import com.soldev.orderservice.infrastructure.adapter.in.rest.mapper.OrderRequestMapper;
import com.soldev.orderservice.infrastructure.adapter.in.rest.request.OrderCreationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderCreatedUseCase createOrderUseCase;
    private final OrderRequestMapper mapper;
    Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderCreatedUseCase createOrderUseCase, OrderRequestMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public void createOrder(@RequestBody OrderCreationRequest orderRequest){
        logger.info("ORDER-CONTROLLER: Received request: {}", orderRequest.toString());
        createOrderUseCase.createOrder(mapper.mapCreationRequest(orderRequest));
    }
}
