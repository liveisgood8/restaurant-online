package com.ro.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthModuleSecurityConfig implements ModuleSecurity {
  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
          .antMatchers("/auth").permitAll()
          .antMatchers("/auth/registration").permitAll();
  }
}
