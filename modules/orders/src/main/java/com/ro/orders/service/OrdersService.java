package com.ro.orders.service;

import com.ro.orders.events.OrderEvent;
import com.ro.orders.model.Order;
import com.ro.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {
  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private ApplicationEventMulticaster eventMulticaster;

  public void makeOrder(Order order) {
    order.getOrderInfos()
        .forEach(x -> x.setOrder(order));

    Order savedOrder = ordersRepository.save(order);

    OrderEvent orderEvent = new OrderEvent(savedOrder, this);
    eventMulticaster.multicastEvent(orderEvent);
  }
}
