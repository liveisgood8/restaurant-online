package com.ro.orders.service;

import com.ro.core.exceptions.RoIllegalArgumentException;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CrudOrdersService {
  private final OrdersRepository ordersRepository;
  private final OrderContactsService orderContactsService;

  @Autowired
  public CrudOrdersService(OrdersRepository ordersRepository,
                           OrderContactsService orderContactsService) {
    this.ordersRepository = ordersRepository;

    this.orderContactsService = orderContactsService;
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
    orderContactsService.saveOrderContacts(order);
    return ordersRepository.save(order);
  }

}
