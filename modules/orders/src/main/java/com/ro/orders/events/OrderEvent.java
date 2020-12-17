package com.ro.orders.events;

import com.ro.orders.data.model.Order;
import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {
  private final Order order;

  public OrderEvent(Order order, Object source) {
    super(source);
    this.order = order;
  }

  public Order getOrder() {
    return order;
  }
}
