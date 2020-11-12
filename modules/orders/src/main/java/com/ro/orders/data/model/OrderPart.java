package com.ro.orders.data.model;

import com.ro.core.data.AbstractModel;
import com.ro.menu.model.Dish;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "order_parts")
public class OrderPart extends AbstractModel {
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

  /**
   * Цена блюда на момент продажи
   */
  @Setter(value = AccessLevel.NONE)
  @Column(name = "selling_price", nullable = false)
  private Short sellingPrice;

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
    if (dish != null) {
      this.id.dishId = dish.getId();
      this.sellingPrice = dish.getPrice();
    }
  }

  public int getTotalPrice() {
    return sellingPrice * count;
  }
}
