package com.ro.menu.model;

import com.ro.menu.model.raw.DishLikes;

public class DishWithImageUrlAndLikes extends Dish {
  private final String imageUrl;

  public DishWithImageUrlAndLikes(Dish dish, String imageUrl) {
    setId(dish.getId());
    setName(dish.getName());
    setCategory(dish.getCategory());
    setDescription(dish.getDescription());
    setProtein(dish.getProtein());
    setFat(dish.getFat());
    setCarbohydrates(dish.getCarbohydrates());
    setWeight(dish.getWeight());
    setPrice(dish.getPrice());
    setLikes(new DishLikes(dish.getEmotions()));
    this.imageUrl = imageUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
