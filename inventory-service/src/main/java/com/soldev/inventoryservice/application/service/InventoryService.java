package com.soldev.inventoryservice.application.service;

import com.soldev.inventoryservice.application.port.in.ProcessOrderUseCase;
import com.soldev.inventoryservice.domain.model.OrderResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryService implements ProcessOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    @Override
    public void process(OrderResolution order) {
        log.info("Ejecutando lógica de negocio: Reservando stock para la orden: {}", order.orderId());

        //Prueba dead letter queue
        //throw new RuntimeException("Simulando error fatal de base de datos");
    }


}
