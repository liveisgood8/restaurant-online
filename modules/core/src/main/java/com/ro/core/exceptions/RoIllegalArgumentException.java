package com.ro.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoIllegalArgumentException extends IllegalArgumentException {
  public RoIllegalArgumentException(String s) {
    super(s);
  }
}
