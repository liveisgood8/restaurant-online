package com.ro.menu.model;

import com.fasterxml.jackson.annotation.*;
import com.ro.menu.validation.InsertGroup;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dishes")
public class Dish {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "name", nullable = false, length = 64)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "protein")
  private Double protein;

  @Column(name = "fat")
  private Double fat;

  @Column(name = "carbohydrates")
  private Double carbohydrates;

  @Column(name = "weight", nullable = false)
  private Short weight;

  @Column(name = "price", nullable = false)
  private Short price;

  @Column(name = "image_path")
  @Basic(fetch = FetchType.LAZY)
  private String imagePath;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "dish")
  private Set<DishEmotion> emotions = Collections.emptySet();
}
