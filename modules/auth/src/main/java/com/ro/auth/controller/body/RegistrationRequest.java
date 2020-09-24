package com.ro.auth.controller.body;

import com.sun.istack.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RegistrationRequest {
  @Pattern(regexp="^((\\+7|7|8)+([0-9]){10})$")
  @NotNull
  private String phone;

  @Email
  @NotNull
  private String email;

  @NotNull
  private String password;

  @Nullable
  private String name;

  public String getPhone() {
    return phone;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }
}
