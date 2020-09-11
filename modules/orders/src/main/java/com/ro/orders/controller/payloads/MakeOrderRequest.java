package com.ro.orders.controller.payloads;

import com.ro.menu.model.Dish;

import java.util.Set;

public class MakeOrderRequest {
  private Set<DishWithCount> entries;

  public static class DishWithCount {
    private Dish dish;
    private Long count;

    public Dish getDish() {
      return dish;
    }

    public void setDish(Dish dish) {
      this.dish = dish;
    }

    public Long getCount() {
      return count;
    }

    public void setCount(Long count) {
      this.count = count;
    }
  }

  public Set<DishWithCount> getEntries() {
    return entries;
  }

  public void setEntries(Set<DishWithCount> entries) {
    this.entries = entries;
  }
}
