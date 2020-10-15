package com.ro.auth.controller.body;

import com.ro.auth.dto.objects.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResponse {
  private final String accessToken;
  private final UserDto user;
}
