package com.ro.auth.data.model;

import com.ro.core.data.model.BaseEnumEntity;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority extends BaseEnumEntity {
  public static final String ADMIN = "ADMIN";
}
