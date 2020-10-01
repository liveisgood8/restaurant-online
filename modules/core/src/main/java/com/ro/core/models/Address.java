package com.ro.core.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {
  @Id
  @GeneratedValue
  Long id;

  @Column(nullable = false)
  private String street;

  @Column(nullable = false)
  private Integer homeNumber;

  @Column(nullable = false)
  private Integer entranceNumber;

  @Column(nullable = false)
  private Integer floorNumber;

  @Column(nullable = false)
  private Integer apartmentNumber;
}
