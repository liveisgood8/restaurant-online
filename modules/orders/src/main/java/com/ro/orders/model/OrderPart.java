package com.ro.orders.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ro.menu.model.Dish;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = OrderPart.class)
@Entity
@Table(name = "order_parts")
public class OrderPart {
  @Embeddable
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
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

  @Setter(value = AccessLevel.NONE)
  @Column(name = "total_price", nullable = false)
  private Integer totalPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("orderId")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("dishId")
  private Dish dish;

  public void setOrder(Order order) {
    this.order = order;
    if (order != null) {
      this.id.orderId = order.getId();
    }
  }

  public void setDish(Dish dish) {
    this.dish = dish;
    recalculateTotalPrice();
  }

  public void setCount(Integer count) {
    this.count = count;
    recalculateTotalPrice();
  }

  private void recalculateTotalPrice() {
    if (dish == null || dish.getPrice() == null || count == null) {
      return;
    }
    totalPrice = dish.getPrice() * count;
  }
}
