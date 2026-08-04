package com.soldev.orderservice.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {

    private Long id;
    private String customerId;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

}
