package com.ro.auth.controller.body;

import com.ro.auth.data.dto.objects.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResponse {
  private final UserDto user;
  private final String accessToken;
}
