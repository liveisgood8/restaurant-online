package com.ro.analytics.data.dto.object;

import lombok.Data;

@Data
public class DishOrdersStatisticDto {
  private MinimalDishDto minimalDish;
  private int ordersCount;
}
