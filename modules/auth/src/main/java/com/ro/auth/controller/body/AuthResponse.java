package com.ro.auth.controller.body;

import com.ro.auth.model.User;

public class AuthResponse {
  private final String accessToken;
  private final UserInfo userInfo;

  public static class UserInfo {
    private final Long id;
    private final String phone;
    private final String email;
    private final String name;
    private final Integer bonuses;

    public UserInfo(User user) {
      this.id = user.getId();
      this.phone = user.getPhone();
      this.email = user.getEmail();
      this.name = user.getName();
      this.bonuses = user.getBonuses();
    }

    public Long getId() {
      return id;
    }

    public String getPhone() {
      return phone;
    }

    public String getEmail() {
      return email;
    }

    public String getName() {
      return name;
    }

    public Integer getBonuses() {
      return bonuses;
    }
  }


  public AuthResponse(String accessToken, UserInfo userInfo) {
    this.accessToken = accessToken;
    this.userInfo = userInfo;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }
}
