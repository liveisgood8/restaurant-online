package com.ro.orders.dto.objects;

import lombok.Data;

@Data
public class AddressDto {
  private Long id;
  private String street;
  private Short homeNumber;
  private Short entranceNumber;
  private Short floorNumber;
  private Integer apartmentNumber;
}
