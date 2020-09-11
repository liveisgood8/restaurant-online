package com.ro.orders.service;

import com.ro.auth.model.User;
import com.ro.orders.controller.payloads.MakeOrderRequest;
import com.ro.orders.events.OrderEvent;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderInfo;
import com.ro.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrdersService {
  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private ApplicationEventMulticaster eventMulticaster;

  public Order makeOrder(MakeOrderRequest makeOrderRequest, User user) {
    Order order = new Order();
    order.setUser(user);

    Set<OrderInfo> orderInfos = makeOrderRequest.getEntries().stream()
        .map(entry -> {
          OrderInfo orderInfo = new OrderInfo();
          orderInfo.setDish(entry.getDish());
          orderInfo.setCount(entry.getCount());
          orderInfo.setOrder(order);
          return orderInfo;
        })
        .collect(Collectors.toSet());
    order.setOrderInfos(orderInfos);

    Order savedOrder = ordersRepository.save(order);

    OrderEvent orderEvent = new OrderEvent(savedOrder, this);
    eventMulticaster.multicastEvent(orderEvent);

    return savedOrder;
  }
}
