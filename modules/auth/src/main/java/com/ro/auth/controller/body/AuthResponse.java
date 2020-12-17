package com.ro.auth.controller.body;

import com.ro.auth.data.dto.objects.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {
  private final UserDto user;
  private final String accessToken;
}
