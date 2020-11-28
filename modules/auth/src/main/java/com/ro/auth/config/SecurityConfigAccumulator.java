package com.ro.auth.config;

import com.ro.core.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.List;

@Configuration
public class SecurityConfigAccumulator {
  private final ApiConfig apiConfig;
  private final List<ModuleSecurity> modulesSecurity;

  @Autowired
  public SecurityConfigAccumulator(ApiConfig apiConfig, List<ModuleSecurity> modulesSecurity) {
    this.apiConfig = apiConfig;
    this.modulesSecurity = modulesSecurity;
  }

  public void apply(HttpSecurity httpSecurity) throws Exception {
    for (ModuleSecurity moduleSecurity : modulesSecurity) {
      moduleSecurity.configure(httpSecurity);
    }

    httpSecurity
        .authorizeRequests()
          .antMatchers(apiConfig.getBasePath() + "/**").authenticated();
  }
}
