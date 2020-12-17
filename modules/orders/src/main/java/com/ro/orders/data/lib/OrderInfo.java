package com.ro.orders.data.lib;

import com.ro.orders.data.model.Order;
import lombok.Data;

@Data
public class OrderInfo {
  public final Order order;
  public final Integer receivedBonuses;
}
