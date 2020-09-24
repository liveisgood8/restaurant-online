package com.ro.menu.model;

import com.fasterxml.jackson.annotation.*;
import com.ro.menu.model.raw.DishLikes;
import com.ro.menu.validation.InsertGroup;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
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

  @Column
  private Double protein;

  @Column
  private Double fat;

  @Column
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

  @JsonIgnore
  @JsonManagedReference
  @OneToMany(mappedBy = "dish")
  private Set<DishEmotion> emotions;

  @Transient
  private DishLikes likes;
}
