package com.ro.auth.controller.body;

import com.sun.istack.Nullable;

import javax.validation.constraints.Email;

public class RegistrationRequest {
  @Email
  private String email;

  private String password;

  @Nullable
  private String name;

  @Nullable
  private String surname;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }
}
