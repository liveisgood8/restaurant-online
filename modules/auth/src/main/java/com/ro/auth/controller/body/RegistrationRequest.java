package com.ro.auth.controller.body;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Getter
public class RegistrationRequest {
  @Pattern(regexp="^((\\+7|7|8)+([0-9]){10})$")
  @NotNull
  private final String phone;

  @Email
  @NotNull
  private final String email;

  @NotNull
  private final String password;

  @Nullable
  private final String name;
}
