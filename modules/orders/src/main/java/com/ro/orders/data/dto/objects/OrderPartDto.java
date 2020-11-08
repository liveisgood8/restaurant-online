package com.ro.orders.data.dto.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ro.core.data.Identity;
import com.ro.menu.dto.objects.DishDto;
import lombok.Data;


@Data
public class OrderPartDto {
  private Long orderId;
  private DishDto dish;
  private Short sellingPrice;
  private Integer count;

  @JsonIgnore
  public int getTotalPrice() {
    return sellingPrice * count;
  }
}
