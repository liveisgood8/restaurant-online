package com.ro.orders.config;

import com.ro.auth.config.ModuleSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class OrdersSecurityConfig implements ModuleSecurity {
  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/orders").permitAll()
        .antMatchers("/orders/**").hasAuthority("ADMIN");
  }
}
