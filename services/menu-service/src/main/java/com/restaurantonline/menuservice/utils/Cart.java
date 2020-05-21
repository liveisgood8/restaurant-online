package com.restaurantonline.menuservice.utils;

import com.restaurantonline.menuservice.model.Dish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
  private List<Dish> dishes = new ArrayList<>();

  public void add(Dish dish) {
    dishes.add(dish);
  }

  public final List<Dish> getAll() {
    return dishes;
  }

  public void clear() {
    dishes.clear();
  }
}
