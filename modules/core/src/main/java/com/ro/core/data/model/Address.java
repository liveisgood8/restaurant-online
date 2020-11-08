package com.ro.core.data.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "addresses")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "street", nullable = false, length = 64)
  private String street;

  @Column(name = "home_number", nullable = false)
  private Short homeNumber;

  @Column(name = "entrance_number", nullable = false)
  private Short entranceNumber;

  @Column(name = "floor_number", nullable = false)
  private Short floorNumber;

  @Column(name = "apartment_number", nullable = false)
  private Integer apartmentNumber;
}
