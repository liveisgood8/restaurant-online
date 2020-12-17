package com.ro.orders.data.dto.objects;

import com.ro.core.data.Identity;
import lombok.Data;

@Data
public class AddressDto implements Identity {
  private Long id;
  private String street;
  private Short homeNumber;
  private Short entranceNumber;
  private Short floorNumber;
  private Integer apartmentNumber;
}
