package com.ro.menu.config;

import com.ro.auth.config.ModuleSecurity;
import com.ro.core.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class MenuSecurityConfig implements ModuleSecurity {
  private final ApiConfig apiConfig;

  @Autowired
  public MenuSecurityConfig(ApiConfig apiConfig) {
    this.apiConfig = apiConfig;
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, apiConfig.getBasePath() + "/menu/dishes/**").permitAll()
        .antMatchers(HttpMethod.GET, apiConfig.getBasePath() + "/menu/categories/**").permitAll()
        .antMatchers(apiConfig.getBasePath() + "/menu/dishes/*/likes/like").authenticated()
        .antMatchers(apiConfig.getBasePath() + "/menu/dishes/*/likes/dislike").authenticated()
        .antMatchers(apiConfig.getBasePath() + "/menu/dishes/**").hasAuthority("ADMIN");
  }
}
