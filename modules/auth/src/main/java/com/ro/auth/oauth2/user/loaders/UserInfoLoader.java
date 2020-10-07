package com.ro.auth.oauth2.user.loaders;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserInfoLoader {
    OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest);
}
