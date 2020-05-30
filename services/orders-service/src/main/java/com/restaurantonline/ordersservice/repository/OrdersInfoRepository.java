package com.restaurantonline.ordersservice.repository;

import com.restaurantonline.ordersservice.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersInfoRepository extends JpaRepository<OrderInfo, Long> {
}
