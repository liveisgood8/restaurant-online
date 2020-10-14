package com.ro.orders.dto.objects;

import lombok.Data;

@Data
public class OrderWithBonusesDto {
  private OrderDto order;
  private Integer bonuses;
}
