package com.ro.orders.repository;

import com.ro.orders.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersInfoRepository extends JpaRepository<OrderInfo, Long> {
}
