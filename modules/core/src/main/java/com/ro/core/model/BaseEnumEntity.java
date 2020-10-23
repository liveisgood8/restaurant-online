package com.ro.core.model;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class BaseEnumEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "tinyint")
  protected Short id;

  @Column(name = "name", nullable = false, length = 64)
  protected String name;
}
