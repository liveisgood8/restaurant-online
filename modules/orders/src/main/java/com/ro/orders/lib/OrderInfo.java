package com.ro.orders.lib;

import com.ro.orders.model.Order;
import lombok.Data;

@Data
public class OrderInfo {
  public final Order order;
  public final Integer receivedBonuses;
}
