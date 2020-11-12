package com.ro.analytics.data.model;

import com.ro.menu.model.Dish;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@Entity
@Immutable
@Table(name = "dish_orders_statistic")
public class DishOrdersStatistic {
  @Id
  @Column(name = "dish_id", nullable = false)
  private Long dishId;

  @MapsId
  @OneToOne
  private Dish dish;

  @Column(name = "orders_count", nullable = false)
  private Integer ordersCount;
}
