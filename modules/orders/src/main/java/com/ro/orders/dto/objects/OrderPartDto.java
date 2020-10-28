package com.ro.orders.dto.objects;

import com.ro.menu.dto.objects.DishDto;
import lombok.Data;
import org.mapstruct.Mapper;


@Data
public class OrderPartDto {
  private Long orderId;
  private DishDto dish;
  private Integer count;
  private Integer totalPrice;
}
