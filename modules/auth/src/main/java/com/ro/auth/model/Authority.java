package com.ro.auth.model;

import com.ro.core.model.BaseEnumEntity;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority extends BaseEnumEntity {
  public static final String ADMIN = "ADMIN";
}
