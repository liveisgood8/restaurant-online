package com.ro.menu.model;

public class DishWithImageUrl extends Dish {
  private final String imageUrl;

  public DishWithImageUrl(Dish dish, String imageUrl) {
    setId(dish.getId());
    setName(dish.getName());
    setCategory(dish.getCategory());
    setDescription(dish.getDescription());
    setProtein(dish.getProtein());
    setFat(dish.getFat());
    setCarbohydrates(dish.getCarbohydrates());
    setWeight(dish.getWeight());
    setPrice(dish.getPrice());
    setLikes(dish.getLikes());
    this.imageUrl = imageUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
