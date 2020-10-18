package com.ro.orders.service;

import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.model.Order;
import com.ro.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrudOrdersService {
  private final OrdersRepository ordersRepository;

  @Autowired
  public CrudOrdersService(OrdersRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  public List<Order> getAll() {
    return ordersRepository.findAll();
  }

  public List<Order> getApproved(boolean isApproved) {
    return ordersRepository.findWithOrderPartsWithBonusesTransactionsAndByIsApproved(isApproved);
  }
}
