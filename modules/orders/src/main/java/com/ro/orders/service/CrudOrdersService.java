package com.ro.orders.service;

import com.ro.core.exceptions.RoIllegalArgumentException;
import com.ro.orders.model.Order;
import com.ro.orders.repository.BonusesTransactionRepository;
import com.ro.orders.repository.OrderPartsRepository;
import com.ro.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
public class CrudOrdersService {
  private final OrdersRepository ordersRepository;
  private final OrderPartsRepository orderPartsRepository;
  private final BonusesTransactionRepository bonusesTransactionRepository;

  @Autowired
  public CrudOrdersService(OrdersRepository ordersRepository,
                           OrderPartsRepository orderPartsRepository,
                           BonusesTransactionRepository bonusesTransactionRepository) {
    this.ordersRepository = ordersRepository;
    this.orderPartsRepository = orderPartsRepository;
    this.bonusesTransactionRepository = bonusesTransactionRepository;
  }

  public List<Order> getAll() {
    return ordersRepository.findAll();
  }

  public List<Order> getApproved(boolean isApproved) {
    return ordersRepository.findByIsApproved(isApproved);
  }

  public Order getWithParts(Long id) {
    return ordersRepository.findWithOrderPartsById(id)
        .orElseThrow(() -> new EntityNotFoundException("Order with id: " + id + " not founded"));
  }

  @Transactional
  public Order update(Long id, Order order) {
    if (!order.getId().equals(id)) {
      throw new RoIllegalArgumentException("Order entity id must be the same as resource id");
    }
    return ordersRepository.save(order);
  }

  @Transactional
  public Order save(Order order) {
    final Order savedOrder = ordersRepository.save(order);

//    order.getOrderParts().forEach(p -> {
//      p.setOrder(savedOrder);
//    });
//    savedOrder.setOrderParts(new HashSet<>(orderPartsRepository.saveAll(order.getOrderParts())));
//
//    if (!order.getBonusesTransactions().isEmpty()) {
//      savedOrder.setBonusesTransactions(new HashSet<>(bonusesTransactionRepository.saveAll(order.getBonusesTransactions())));
//    }

    return savedOrder;
  }
}
