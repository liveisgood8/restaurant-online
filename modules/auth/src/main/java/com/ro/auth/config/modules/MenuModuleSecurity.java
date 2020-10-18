package com.ro.auth.config.modules;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class MenuModuleSecurity {
  public static void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
          .antMatchers(HttpMethod.GET, "/menu/dishes/**").permitAll()
          .antMatchers(HttpMethod.GET, "/menu/categories/**").permitAll()
          .antMatchers("/menu/dishes/*/likes/like").authenticated()
          .antMatchers("/menu/dishes/*/likes/dislike").authenticated()
          .antMatchers("/menu/dishes/**").hasAuthority("ADMIN");
  }
}
