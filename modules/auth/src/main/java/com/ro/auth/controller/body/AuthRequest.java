package com.ro.auth.controller.body;

import lombok.Data;

@Data
public class AuthRequest {
  private String login;
  private String password;
}
