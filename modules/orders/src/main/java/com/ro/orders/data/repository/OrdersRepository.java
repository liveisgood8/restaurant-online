package com.ro.orders.data.repository;

import com.ro.orders.data.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Order, Long> {
  List<Order> findByIsApproved(boolean isApproved);

  @EntityGraph(attributePaths = { "orderParts" })
  Optional<Order> findWithOrderPartsById(Long id);

  @EntityGraph(attributePaths = { "orderParts" })
  Optional<Order> findWithPartsById(Long id);

  @Modifying
  @Query("update Order o set o.isApproved = ?1 where o.id = ?2")
  void setIsApprovedById(boolean isApproved, Long id);
}
