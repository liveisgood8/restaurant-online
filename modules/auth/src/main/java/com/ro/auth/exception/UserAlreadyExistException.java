package com.ro.auth.exception;

public class UserAlreadyExistException extends RuntimeException {
  public UserAlreadyExistException(String email) {
    super("User with email: " + email + " is already exist");
  }
}
