package com.ro.auth.dto.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@JsonIgnoreProperties(value = { "password" }, allowSetters = true)
@Data
public class UserDto {
  private Long id;
  private String email;
  private String name;
  private String password;
  private Integer bonuses;
  private String phone;
  private Set<String> authorities = Collections.emptySet();
}
