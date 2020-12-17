package com.ro.analytics.data.model;

import com.ro.menu.model.Dish;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@Entity
@Immutable
@Table(name = "dish_emotions_statistic")
public class DishEmotionsStatistic {
  @Id
  @Column(name = "dish_id", nullable = false)
  private Long dishId;

  @MapsId
  @OneToOne
  private Dish dish;

  @Column(name = "likes_count", nullable = false)
  private Integer likesCount;

  @Column(name = "dislikes_count", nullable = false)
  private Integer dislikesCount;
}
