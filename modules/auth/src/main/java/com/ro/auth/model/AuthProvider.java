package com.ro.auth.model;

import com.ro.core.model.BaseEnumEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "auth_providers")
public class AuthProvider extends BaseEnumEntity {
  public static final String NATIVE = "NATIVE";
  public static final String GOOGLE = "GOOGLE";
  public static final String VK = "VK";
  public static final String FACEBOOK = "FACEBOOK";
}
