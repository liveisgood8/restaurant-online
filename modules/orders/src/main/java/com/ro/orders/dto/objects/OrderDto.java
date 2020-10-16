package com.ro.orders.dto.objects;

import com.ro.orders.model.Order;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class OrderDto {
  private Long id;
  private Boolean isApproved;
  private Order.PaymentMethod paymentMethod;
  private AddressDto address;
  private String phone;
  private Integer spentBonuses;
  private Integer receivedBonuses;
  private Integer totalPrice;
  private Set<OrderPartDto> orderParts;
  private Date createdAt;

}
