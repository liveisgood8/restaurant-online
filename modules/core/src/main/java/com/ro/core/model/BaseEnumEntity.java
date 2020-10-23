package com.ro.core.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class BaseEnumEntity {
  @Id
  @GeneratedValue
  @Column(name = "id", columnDefinition = "tinyint")
  protected Short id;

  @Column(name = "name", nullable = false, length = 64)
  protected String name;
}
