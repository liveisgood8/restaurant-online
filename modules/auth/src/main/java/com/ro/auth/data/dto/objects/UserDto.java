package com.ro.auth.data.dto.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ro.core.data.Identity;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@JsonIgnoreProperties(value = { "password" }, allowSetters = true)
@Data
public class UserDto implements Identity {
  private Long id;
  private String email;
  private String name;
  private String password;
  private Integer bonuses;
  private String phone;
  private Set<String> authorities = Collections.emptySet();
}
