package com.ro.analytics.config;

import com.ro.auth.config.ModuleSecurity;
import com.ro.core.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AnalyticsSecurityConfig implements ModuleSecurity {
  private final ApiConfig apiConfig;

  @Autowired
  public AnalyticsSecurityConfig(ApiConfig apiConfig) {
    this.apiConfig = apiConfig;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
          .antMatchers(apiConfig.getBasePath() + "/analytics/*").hasAuthority("ADMIN");
  }
}
