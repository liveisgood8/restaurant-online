package com.ro.menu.config;

import com.ro.auth.config.ModuleSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class MenuSecurityConfig implements ModuleSecurity {
  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/menu/dishes/**").permitAll()
        .antMatchers(HttpMethod.GET, "/menu/categories/**").permitAll()
        .antMatchers("/menu/dishes/*/likes/like").authenticated()
        .antMatchers("/menu/dishes/*/likes/dislike").authenticated()
        .antMatchers("/menu/dishes/**").hasAuthority("ADMIN");
  }
}
