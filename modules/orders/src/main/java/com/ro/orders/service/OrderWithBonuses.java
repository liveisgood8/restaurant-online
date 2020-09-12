package com.ro.orders.service;

import com.ro.orders.model.Order;

public class OrderWithBonuses {
  public final Order order;
  public final Integer bonuses;

  public OrderWithBonuses(Order order, Integer bonuses) {
    this.order = order;
    this.bonuses = bonuses;
  }

  public Order getOrder() {
    return order;
  }

  public Integer getBonuses() {
    return bonuses;
  }
}
