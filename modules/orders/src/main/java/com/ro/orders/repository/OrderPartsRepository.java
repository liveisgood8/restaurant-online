package com.ro.orders.repository;

import com.ro.orders.model.OrderPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPartsRepository extends JpaRepository<OrderPart, Long> {
}
