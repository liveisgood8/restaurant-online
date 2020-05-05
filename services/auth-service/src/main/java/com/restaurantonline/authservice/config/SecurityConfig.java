package com.restaurantonline.authservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@EnableGlobalMethodSecurity(prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
@KeycloakConfiguration
@EnableConfigurationProperties(KeycloakSpringBootProperties.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
  /**
   * The Keycloak Admin client that provides the service-account Access-Token
   *
   * @param props
   * @return
   */
  @Bean
  public Keycloak keycloak(KeycloakSpringBootProperties props) {
    Keycloak keycloak = KeycloakBuilder.builder() //
        .serverUrl(props.getAuthServerUrl()) //
        .grantType(OAuth2Constants.PASSWORD)
        .realm(props.getRealm()) //
        .username("admin")
        .password("admin")
        .clientId("admin-cli") //
        .build();

    return keycloak;
  }

  /**
   * Registers the KeycloakAuthenticationProvider with the authentication manager.
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(keycloakAuthenticationProvider());
  }

  /**
   * Defines the session authentication strategy.
   */
  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Bean
  public KeycloakConfigResolver KeycloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http
        .csrf().disable()
        .authorizeRequests()
        .anyRequest().permitAll();
  }
}