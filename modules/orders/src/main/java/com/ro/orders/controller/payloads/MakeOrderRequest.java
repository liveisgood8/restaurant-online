package com.ro.orders.controller.payloads;

import com.ro.menu.model.Dish;
import com.ro.orders.model.Order;
import lombok.Getter;

import java.util.Set;

@Getter
public class MakeOrderRequest {
  private String street;
  private Integer homeNumber;
  private Integer entranceNumber;
  private Integer floorNumber;
  private Integer apartmentNumber;
  private Boolean isApproved;
  private Order.PaymentMethod paymentMethod;
  private Set<DishWithCount> entries;

  public static class DishWithCount {
    private Dish dish;
    private Integer count;

    public Dish getDish() {
      return dish;
    }

    public void setDish(Dish dish) {
      this.dish = dish;
    }

    public Integer getCount() {
      return count;
    }

    public void setCount(Integer count) {
      this.count = count;
    }
  }
}
