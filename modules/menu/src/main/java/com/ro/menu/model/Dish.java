package com.ro.menu.model;

import com.fasterxml.jackson.annotation.*;
import com.ro.menu.validation.InsertGroup;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "dishes")
public class Dish {
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

  @Column()
  private Double protein;

  @Column()
  private Double fat;

  @Column()
  private Double carbohydrates;

  @Column(nullable = false)
  private Integer weight;

  @Column(nullable = false)
  private Integer price;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @Column
  @Basic(fetch = FetchType.LAZY)
  private String imagePath;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @JsonManagedReference
  @OneToOne(mappedBy = "dish")
  private DishLikes likes;

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

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public DishLikes getLikes() {
    return likes;
  }

  public void setLikes(DishLikes likes) {
    this.likes = likes;
  }
}
