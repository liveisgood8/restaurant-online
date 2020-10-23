package com.ro.auth.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2ProviderNotExistException extends AuthenticationException {
  public OAuth2ProviderNotExistException(String providerName) {
    super("OAuth 2 provider '" + providerName + "' is not exist");
  }
}
