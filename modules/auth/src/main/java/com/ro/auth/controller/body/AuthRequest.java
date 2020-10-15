package com.ro.auth.controller.body;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthRequest {
  private final String login;
  private final String password;
}
