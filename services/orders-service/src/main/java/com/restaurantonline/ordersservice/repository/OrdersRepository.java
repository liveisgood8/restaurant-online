package com.restaurantonline.ordersservice.repository;

import com.restaurantonline.ordersservice.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrdersRepository extends CrudRepository<Order, Long> {
}
