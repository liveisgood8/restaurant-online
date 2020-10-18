package com.ro.auth.config.modules;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class OrdersModuleSecurity {
  public static void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/orders").permitAll()
        .antMatchers("/orders/**").hasAuthority("ADMIN");
  }
}
