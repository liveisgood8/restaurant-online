package com.ro.orders.config;

import com.ro.auth.config.ModuleSecurity;
import com.ro.core.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class OrdersSecurityConfig implements ModuleSecurity {
  private final ApiConfig apiConfig;

  @Autowired
  public OrdersSecurityConfig(ApiConfig apiConfig) {
    this.apiConfig = apiConfig;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, apiConfig.getBasePath() + "/orders").permitAll()
        .antMatchers(apiConfig.getBasePath() + "/orders/**").hasAuthority("ADMIN");
  }
}
