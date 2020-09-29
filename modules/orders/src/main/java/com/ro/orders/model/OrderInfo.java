package com.ro.orders.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ro.menu.model.Dish;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = OrderInfo.class)
@Entity
@Table(name = "orders_info")
public class OrderInfo {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "count", nullable = false)
  private Integer count;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "dish_id", nullable = false)
  private Dish dish;
}
