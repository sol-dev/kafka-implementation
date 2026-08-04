package com.soldev.orderservice.infrastructure.adapter.in.rest.mapper;

import com.soldev.orderservice.domain.model.Order;
import com.soldev.orderservice.infrastructure.adapter.in.rest.request.OrderCreationRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderRequestMapper {

    public Order mapCreationRequest(OrderCreationRequest request){
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setTotalAmount(request.getTotalAmount());
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

}
