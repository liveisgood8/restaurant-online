package com.ro.orders.service;

import com.ro.auth.model.User;
import com.ro.auth.service.UserService;
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
  private final OrdersRepository ordersRepository;
  private final UserService userService;
  private final ApplicationEventMulticaster eventMulticaster;

  @Autowired
  public OrdersService(OrdersRepository ordersRepository,
                       UserService userService,
                       ApplicationEventMulticaster eventMulticaster) {
    this.ordersRepository = ordersRepository;
    this.userService = userService;
    this.eventMulticaster = eventMulticaster;
  }

  public OrderWithBonuses makeOrder(MakeOrderRequest makeOrderRequest, User user) {
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
    order.setStreet(makeOrderRequest.getStreet());
    order.setHomeNumber(makeOrderRequest.getHomeNumber());
    order.setEntranceNumber(makeOrderRequest.getEntranceNumber());
    order.setFloorNumber(makeOrderRequest.getFloorNumber());
    order.setApartmentNumber(makeOrderRequest.getApartmentNumber());
    order.setPaymentMethod(makeOrderRequest.getPaymentMethod());
    order.setIsApproved(makeOrderRequest.getPaymentMethod() == Order.PaymentMethod.BY_CARD_ONLINE);
    order.setOrderInfos(orderInfos);

    Order savedOrder = ordersRepository.save(order);
    Integer bonuses = creditBonusesToUser(savedOrder);

    OrderEvent orderEvent = new OrderEvent(savedOrder, this);
    eventMulticaster.multicastEvent(orderEvent);

    return new OrderWithBonuses(order, bonuses);
  }


  private Integer creditBonusesToUser(Order order) {
    Integer orderBonuses = calculateBonuses(order);
    userService.addBonuses(order.getUser().getId(), orderBonuses);
    return orderBonuses;
  }

  private Integer calculateBonuses(Order order) {
    return 100;
  }
}
