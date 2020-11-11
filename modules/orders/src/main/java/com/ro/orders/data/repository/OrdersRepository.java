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

  @Query("select distinct o from Order o inner join fetch o.orderParts op inner join fetch op.dish " +
      "where o.isApproved = true")
  List<Order> findAllApprovedWithFullyFetchedParts();

  @EntityGraph(attributePaths = { "orderParts" })
  Optional<Order> findWithOrderPartsById(Long id);
}
