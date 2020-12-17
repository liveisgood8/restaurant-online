package com.ro.core.data.model;

import com.ro.core.data.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public class BaseEnumEntity extends AbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "tinyint")
  protected Short id;

  @Column(name = "name", nullable = false, length = 64)
  protected String name;
}
