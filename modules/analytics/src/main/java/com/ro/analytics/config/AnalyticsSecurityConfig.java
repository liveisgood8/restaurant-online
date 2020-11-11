package com.ro.analytics.config;

import com.ro.auth.config.ModuleSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AnalyticsSecurityConfig implements ModuleSecurity {

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
          .antMatchers("/analytics/*").hasAuthority("ADMIN");
  }
}
