package com.ro.auth.data.lib;

import com.ro.auth.data.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class AuthenticateData {
  private final User user;
  private final String accessToken;
}
