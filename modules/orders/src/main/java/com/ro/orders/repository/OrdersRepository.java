package com.ro.orders.repository;

import com.ro.orders.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrdersRepository extends CrudRepository<Order, Long> {
}
