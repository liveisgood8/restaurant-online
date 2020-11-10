package com.ro.analytics.data.dto.object;

import com.ro.menu.dto.objects.DishDto;
import lombok.Data;

@Data
public class DishOrderStatisticDto {
  @Data
  public static class MinimalDish {
    private Long id;
    private String name;
  }
  private MinimalDish minimalDish;
  private int numberOfOrders;
}
