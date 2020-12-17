package com.ro.auth.config;

import com.ro.core.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthModuleSecurityConfig implements ModuleSecurity {
  private final ApiConfig apiConfig;

  @Autowired
  public AuthModuleSecurityConfig(ApiConfig apiConfig) {
    this.apiConfig = apiConfig;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
          .antMatchers(apiConfig.getBasePath() + "/auth").permitAll()
          .antMatchers(apiConfig.getBasePath() + "/auth/registration").permitAll();
  }
}
