package com.restaurantonline.ordersservice.events.listeners;

import com.restaurantonline.ordersservice.events.OrderEvent;
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
