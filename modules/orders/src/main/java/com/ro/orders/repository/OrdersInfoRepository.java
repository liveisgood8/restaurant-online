package com.ro.orders.repository;

import com.ro.orders.model.OrderPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersInfoRepository extends JpaRepository<OrderPart, Long> {
}
