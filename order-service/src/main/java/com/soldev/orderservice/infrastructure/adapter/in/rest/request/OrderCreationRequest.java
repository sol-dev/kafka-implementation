package com.soldev.orderservice.infrastructure.adapter.in.rest.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreationRequest {

    private String id;
    private String customerId;
    private BigDecimal totalAmount;

}

