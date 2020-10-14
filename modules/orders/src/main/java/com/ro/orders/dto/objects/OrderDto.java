package com.ro.orders.dto.objects;

import com.ro.core.models.Address;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderInfo;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class OrderDto {
  private Long id;
  private Boolean isApproved;
  private Order.PaymentMethod paymentMethod;
  private Address address;
  private Set<OrderInfoDto> orderInfos;
  private Date createdAt;
}
