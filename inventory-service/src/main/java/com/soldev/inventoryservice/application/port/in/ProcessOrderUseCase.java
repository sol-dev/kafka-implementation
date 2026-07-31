package com.soldev.inventoryservice.application.port.in;

import com.soldev.inventoryservice.domain.model.OrderResolution;

public interface ProcessOrderUseCase {
    void process(OrderResolution orderResolution);
}
