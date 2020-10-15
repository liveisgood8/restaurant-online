package com.ro.core.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "addresses")
public class Address {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, length = 64)
  private String street;

  @Column(nullable = false)
  private Short homeNumber;

  @Column(nullable = false)
  private Short entranceNumber;

  @Column(nullable = false)
  private Short floorNumber;

  @Column(nullable = false)
  private Integer apartmentNumber;
}
