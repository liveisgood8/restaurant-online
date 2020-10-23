package com.ro.core.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "telephone_numbers", indexes = {
    @Index(name = "unique full telephone number", columnList = "country_code,telephone_number", unique = true)
})
public class TelephoneNumber {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "country_code", length = 3)
  private String countryCode;

  @Column(name = "telephone_number", length = 15)
  private String nationalNumber;
}
