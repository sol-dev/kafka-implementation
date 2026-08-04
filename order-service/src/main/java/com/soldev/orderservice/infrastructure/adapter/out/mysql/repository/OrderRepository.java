package com.soldev.orderservice.infrastructure.adapter.out.mysql.repository;

import com.soldev.orderservice.infrastructure.adapter.out.mysql.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
