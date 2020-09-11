package com.ro.orders.events.listeners;

import com.ro.auth.service.UserService;
import com.ro.orders.events.OrderEvent;
import com.ro.orders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener implements ApplicationListener<OrderEvent> {
  private final UserService userService;

  @Autowired
  public OrderEventListener(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(OrderEvent event) {
    Order order = event.getOrder();
    creditBonusesToUser(order);
  }


  private void creditBonusesToUser(Order order) {
    Integer orderBonuses = calculateBonuses(order);
    userService.addBonuses(order.getUser().getId(), orderBonuses);
  }

  private Integer calculateBonuses(Order order) {
    return 100;
  }
}
