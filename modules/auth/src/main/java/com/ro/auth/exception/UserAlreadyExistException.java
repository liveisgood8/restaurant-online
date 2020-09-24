package com.ro.auth.exception;

import com.ro.auth.model.User;

public class UserAlreadyExistException extends RuntimeException {
  public UserAlreadyExistException() {
    super("Пользователь с указанным адресом эл. почты или номером телефона уже существует");
  }
}
