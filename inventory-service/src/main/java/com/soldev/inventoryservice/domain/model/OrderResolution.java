package com.soldev.inventoryservice.domain.model;

import java.math.BigDecimal;

public record OrderResolution(Long orderId, String customerId, BigDecimal totalAmount) {}