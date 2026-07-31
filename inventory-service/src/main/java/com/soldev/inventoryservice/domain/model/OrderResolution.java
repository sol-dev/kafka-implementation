package com.soldev.inventoryservice.domain.model;

import java.math.BigDecimal;

public record OrderResolution(String orderId, String customerId, BigDecimal totalAmount) {}