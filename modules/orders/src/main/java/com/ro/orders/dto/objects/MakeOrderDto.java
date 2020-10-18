package com.ro.orders.dto.objects;

import com.ro.menu.dto.objects.DishDto;
import com.ro.orders.model.Order;
import lombok.Data;

import java.util.Set;

@Data
public class MakeOrderDto {
  @Data
  public static class OrderPartDto {
    private Long orderId;
    private Long dishId;
    private Integer count;
  }

  private Order.PaymentMethod paymentMethod;
  private AddressDto address;
  private String phone;
  private Integer spentBonuses;
  private Integer receivedBonuses;
  private Integer totalPrice;
  private Set<OrderPartDto> orderParts;
}
