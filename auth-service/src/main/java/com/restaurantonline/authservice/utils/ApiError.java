package com.restaurantonline.authservice.utils;

public class ApiError {
  private String message;

  ApiError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
