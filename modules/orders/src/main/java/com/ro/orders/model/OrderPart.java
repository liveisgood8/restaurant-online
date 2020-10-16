package com.ro.orders.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ro.menu.model.Dish;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = OrderPart.class)
@Entity
@Table(name = "orders_info")
public class OrderPart {
  @Embeddable
  @Data
  public static class OrderInfoId implements Serializable {
    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;
  }

  @EmbeddedId
  private OrderInfoId id = new OrderInfoId();

  @Column(name = "count", nullable = false)
  private Integer count;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("orderId")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("dishId")
  private Dish dish;
}
