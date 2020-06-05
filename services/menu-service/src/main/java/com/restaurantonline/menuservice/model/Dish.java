package com.restaurantonline.menuservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurantonline.menuservice.validation.InsertGroup;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "dishes")
public class Dish implements Serializable {
  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty(groups = InsertGroup.class)
  @Size(max = 255)
  @Column(nullable = false)
  private String name;

  @NotEmpty(groups = InsertGroup.class)
  @Size(max = 255)
  @Column(nullable = false)
  private String description;

  @Column(nullable = true)
  private Double protein;

  @Column(nullable = true)
  private Double fat;

  @Column(nullable = true)
  private Double carbohydrates;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @Column
  @Basic(fetch = FetchType.LAZY)
  private String imagePath;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getProtein() {
    return protein;
  }

  public void setProtein(Double protein) {
    this.protein = protein;
  }

  public Double getFat() {
    return fat;
  }

  public void setFat(Double fat) {
    this.fat = fat;
  }

  public Double getCarbohydrates() {
    return carbohydrates;
  }

  public void setCarbohydrates(Double carbohydrates) {
    this.carbohydrates = carbohydrates;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
