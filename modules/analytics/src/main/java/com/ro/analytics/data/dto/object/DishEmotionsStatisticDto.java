package com.ro.analytics.data.dto.object;

import lombok.Data;

@Data
public class DishEmotionsStatisticDto {
  private MinimalDishDto minimalDish;
  private int likesCount;
  private int dislikesCount;
}
