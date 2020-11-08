package com.ro.orders.data.dto.objects;

import com.ro.core.data.Identity;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class OrderDto implements Identity {
  private Long id;
  private Boolean isApproved;
  private String paymentMethod;
  private AddressDto address;
  private String phone;
  private Integer spentBonuses;
  private Integer receivedBonuses;
  private Integer totalPrice;
  private Set<OrderPartDto> orderParts;
  private Date createdAt;
}
