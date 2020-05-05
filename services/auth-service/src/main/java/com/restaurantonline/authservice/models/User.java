package com.restaurantonline.authservice.models;


import com.restaurantonline.authservice.validation.InsertGroup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {
  @NotNull
  @NotEmpty(groups = {InsertGroup.class}, message = "Логин пользователя не может быть пустым")
  @Size(min = 5, message = "Длина логина не может быть меньше 5 символов")
  private String login;

  @NotNull
  @NotEmpty(groups = {InsertGroup.class}, message = "Пароль не может быть пустым")
  private String password;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
