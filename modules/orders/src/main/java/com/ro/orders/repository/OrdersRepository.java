package com.ro.orders.repository;

import com.ro.orders.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Long> {
  @EntityGraph(attributePaths = { "orderParts" })
  List<Order> findWithOrderPartsWithBonusesTransactionsAndByIsApproved(boolean isApproved);

  @Modifying
  @Query("update Order o set o.isApproved = ?1 where o.id = ?2")
  void setIsApprovedById(boolean isApproved, Long id);
}
