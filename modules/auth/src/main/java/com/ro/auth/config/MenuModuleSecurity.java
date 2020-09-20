package com.ro.auth.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class MenuModuleSecurity {
  public static void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/menu/dishes/**").permitAll()
        .antMatchers(HttpMethod.GET, "/menu/categories/**").permitAll()
        .antMatchers("/menu/dishes/**").hasAuthority("ADMIN");
  }
}
