package com.restaurantonline.menuservice.model;

public class DishWithImageUrl extends Dish {
  private String imageUrl;

  public DishWithImageUrl(Dish dish, String imageUrl) {
    setId(dish.getId());
    setName(dish.getName());
    setCategory(dish.getCategory());
    setDescription(dish.getDescription());
    setProtein(dish.getProtein());
    setFat(dish.getFat());
    setCarbohydrates(dish.getCarbohydrates());
    this.imageUrl = imageUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
