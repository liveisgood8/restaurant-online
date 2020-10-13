package com.ro.menu.dto.objects;

import lombok.Data;

@Data
public class DishDto {
  private Long id;
  private String name;
  private String description;
  private Double protein;
  private Double fat;
  private Double carbohydrates;
  private Integer weight;
  private Integer price;
  private String imageUrl;
  private DishLikesDto likes;
}
