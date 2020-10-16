package com.ro.orders.dto.objects;

import lombok.Data;

@Data
public class OrderPartDto {
  private Long orderId;
  private Long dishId;
  private Integer count;
}
