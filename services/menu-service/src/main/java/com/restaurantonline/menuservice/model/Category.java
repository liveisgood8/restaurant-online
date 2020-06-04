package com.restaurantonline.menuservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity(name = "category")
@Table(name = "categories", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
public class Category {
  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Length(max = 128)
  @Column(length = 128, nullable = false)
  private String name;

  @URL(message = "Некорректная ссылка на изображение категории")
  @Column
  private String imageUrl;

  @JsonManagedReference
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category")
  private List<Dish> dishes;

  public List<Dish> getDishes() {
    return dishes;
  }

  public void setDishes(List<Dish> dishes) {
    this.dishes = dishes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
