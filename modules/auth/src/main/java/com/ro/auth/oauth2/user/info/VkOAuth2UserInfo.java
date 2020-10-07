package com.ro.auth.oauth2.user.info;

import java.util.Map;

public class VkOAuth2UserInfo extends OAuth2UserInfo {

  public VkOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getName() {
    return (String) attributes.get("first_name");
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }
}
