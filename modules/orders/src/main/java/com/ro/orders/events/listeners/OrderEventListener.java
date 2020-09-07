package com.ro.orders.events.listeners;

import com.ro.orders.events.OrderEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener implements ApplicationListener<OrderEvent> {
  @Override
  public void onApplicationEvent(OrderEvent event) {
    System.out.println("new order event");
    System.out.println(event.getOrder().getId());
  }
}
