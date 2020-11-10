package com.ro.analytics.data.lib;

import com.ro.menu.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DishOrderStatistic {
  private final Dish dish;
  private final int numberOfOrders;
}
