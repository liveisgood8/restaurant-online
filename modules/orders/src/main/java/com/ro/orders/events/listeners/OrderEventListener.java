package com.ro.orders.events.listeners;

import com.ro.orders.events.OrderEvent;
import com.ro.orders.data.model.Order;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener implements ApplicationListener<OrderEvent> {
  @Override
  public void onApplicationEvent(OrderEvent event) {
    Order order = event.getOrder();
  }
}
